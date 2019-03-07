package com.rabidgremlin.concord.resources;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.security.PermitAll;
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

@PermitAll
@Path("stats")
@Produces(MediaType.APPLICATION_JSON)
public class StatsResource
{

  private final StatsDao statsDao;

  private final Logger log = LoggerFactory.getLogger(StatsResource.class);

  public StatsResource(StatsDao statsDao)
  {
    this.statsDao = statsDao;
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
  public Response getUserStats(@ApiParam(hidden = true) @Auth Caller caller)
  {
    log.info("{} getting user stats.", caller);

    List<UserVoteCount> totalVoteCounts = statsDao.getCountOfTotalVotesPerUser();
    List<UserVoteCount> completedVoteCounts = statsDao.getCountOfCompletedVotesPerUser();
    List<UserVoteCount> trashedVoteCounts = statsDao.getCountOfTrashVotesPerUser();
    List<UserVoteCount> totalVoteCountsForPhrasesWithConsensus = statsDao.getCountOfTotalVotesWithConsensusPerUser();
    List<UserVoteCount> completedVoteCountsIgnoringTrash = statsDao.getCountOfCompletedVotesPerUserIgnoringTrash();
    List<UserVoteCount> totalVotesForPhrasesWithConsensusIgnoringTrash = statsDao
        .getCountOfTotalVotesWithConsensusPerUserIgnoringTrash();

    List<UserStats> userStats = totalVoteCounts.stream()
        // only include users who have voted
        .filter(userVoteCount -> userVoteCount.getVoteCount() > 0)
        .map(totalVoteCount -> {
          String userId = totalVoteCount.getUserId();
          int total = totalVoteCount.getVoteCount();
          int completed = getVotesForUser(completedVoteCounts, userId);
          int trashed = getVotesForUser(trashedVoteCounts, userId);
          int totalWithConsensus = getVotesForUser(totalVoteCountsForPhrasesWithConsensus, userId);
          int completedIgnoringTrash = getVotesForUser(completedVoteCountsIgnoringTrash, userId);
          int totalWithConsensusIgnoringTrash = getVotesForUser(totalVotesForPhrasesWithConsensusIgnoringTrash, userId);
          return new UserStats(userId, total, completed, trashed, totalWithConsensus, completedIgnoringTrash, totalWithConsensusIgnoringTrash);
        })
        .sorted(Comparator.comparing(UserStats::getTotalVotes).reversed())
        .collect(Collectors.toList());

    return Response.ok().entity(userStats).build();
  }

}
