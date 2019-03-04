package com.rabidgremlin.concord.resources;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;
import com.rabidgremlin.concord.api.UserStats;
import com.rabidgremlin.concord.api.UserVoteCount;
import com.rabidgremlin.concord.auth.Caller;
import com.rabidgremlin.concord.dao.StatsDao;

import io.dropwizard.auth.Auth;
import io.swagger.annotations.ApiParam;

@Path("stats")
@Produces(MediaType.APPLICATION_JSON)
public class StatsResource
{

  private static final String USER_TO_IGNORE = "BULK_UPLOAD";

  private final StatsDao statsDao;

  private final int consensusLevel;

  private final Logger log = LoggerFactory.getLogger(StatsResource.class);

  public StatsResource(StatsDao statsDao, int consensusLevel)
  {
    this.statsDao = statsDao;
    this.consensusLevel = consensusLevel;
  }

  private List<UserVoteCount> withoutIgnoredUsers(List<UserVoteCount> list)
  {
    return list.stream()
        .filter(u -> !u.getUserId().equals(USER_TO_IGNORE))
        .collect(Collectors.toList());
  }

  private int getVotesForUser(List<UserVoteCount> list, String userId)
  {
    return list.stream()
        .filter(u -> u.getUserId().equals(userId))
        .findFirst()
        .map(UserVoteCount::getVoteCount)
        .orElse(0);
  }

  @GET
  @Timed
  @Path("/")
  public Response getUserStats(@ApiParam(hidden = true) @Auth Caller caller)
  {
    log.info("{} getting all user stats.", caller);
    log.debug("Consensus level = {}", consensusLevel);

    List<UserVoteCount> totalVoteCounts = withoutIgnoredUsers(statsDao.getTotalCountOfVotesMadePerUser());
    List<UserVoteCount> completedVoteCounts = withoutIgnoredUsers(statsDao.getCompletedCountOfVotesMadePerUser());
    List<UserVoteCount> trashedVoteCounts = withoutIgnoredUsers(statsDao.getCountOfTrashVotesPerUser());
    List<UserVoteCount> totalVoteCountsForPhrasesBeyondConsensus = withoutIgnoredUsers(
        statsDao.getCountOfVotesMadePerUserForPhrasesBeyondVoteMargin(consensusLevel));

    List<UserStats> userStats = totalVoteCounts.stream()
        // only include users who have voted (also avoids divide by zero)
        .filter(totalVoteCount -> totalVoteCount.getVoteCount() > 0)
        .map(totalVoteCount -> {
          String userId = totalVoteCount.getUserId();
          int total = totalVoteCount.getVoteCount();
          int completed = getVotesForUser(completedVoteCounts, userId);
          int trashed = getVotesForUser(trashedVoteCounts, userId);
          int totalBeyondConsensus = getVotesForUser(totalVoteCountsForPhrasesBeyondConsensus, userId);
          float trashRatio = (float) trashed / total;
          float completedSuccessRatio = totalBeyondConsensus > 0 ? (float) completed / totalBeyondConsensus : 0;
          return new UserStats(userId, total, completed, trashed, completedSuccessRatio, trashRatio);
        })
        .sorted(Comparator.comparing(UserStats::getTotalVotes).reversed())
        .collect(Collectors.toList());

    userStats.stream().map(UserStats::toString).forEach(log::info);

    return Response.ok().entity(userStats).build();
  }

}
