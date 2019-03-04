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
import com.google.common.collect.Streams;
import com.rabidgremlin.concord.api.UserVoteCount;
import com.rabidgremlin.concord.api.UserVoteRatio;
import com.rabidgremlin.concord.auth.Caller;
import com.rabidgremlin.concord.dao.StatsDao;

import io.dropwizard.auth.Auth;
import io.swagger.annotations.ApiParam;

@Path("stats/votes")
@Produces(MediaType.APPLICATION_JSON)
public class StatsResource
{

  private static final String USER_TO_IGNORE = "BULK_UPLOAD";

  private final StatsDao statsDao;

  private final Logger log = LoggerFactory.getLogger(StatsResource.class);

  public StatsResource(StatsDao votesDao)
  {
    this.statsDao = votesDao;
  }

  private List<UserVoteCount> withoutIgnoredUsers(List<UserVoteCount> list)
  {
    return list.stream()
        .filter(s -> !s.getUserId().equals(USER_TO_IGNORE))
        .collect(Collectors.toList());
  }

  @GET
  @Timed
  @Path("/total")
  public Response getTotalUserVotes(@ApiParam(hidden = true) @Auth Caller caller)
  {
    log.info("{} getting total count of user votes.", caller);

    List<UserVoteCount> totalVoteCounts = withoutIgnoredUsers(statsDao.getTotalCountOfVotesMadePerUser());

    totalVoteCounts.stream().map(UserVoteCount::toString).forEach(log::info);

    return Response.ok().entity(totalVoteCounts).build();
  }

  @GET
  @Timed
  @Path("/completed")
  public Response getCompletedUserVotes(@ApiParam(hidden = true) @Auth Caller caller)
  {
    log.info("{} getting count of completed user votes.", caller);

    List<UserVoteCount> completedVoteCounts = withoutIgnoredUsers(statsDao.getCompletedCountOfVotesMadePerUser());

    completedVoteCounts.stream().map(UserVoteCount::toString).forEach(log::info);

    return Response.ok().entity(completedVoteCounts).build();
  }

  @GET
  @Timed
  @Path("/completed/ratio")
  public Response getCompletedRatioOfUserVotesForPhrasesBeyondConsensus(@ApiParam(hidden = true) @Auth Caller caller)
  {
    log.info("{} getting ratio of completed user votes for phrases beyond consensus.", caller);

    List<UserVoteCount> completedVoteCounts = withoutIgnoredUsers(statsDao.getCompletedCountOfVotesMadePerUser());
    List<UserVoteCount> totalVoteCountsForPhrasesBeyondConsensus = withoutIgnoredUsers(statsDao.getCountOfVotesMadePerUserForPhrasesBeyondVoteMargin(3));

    completedVoteCounts.sort(Comparator.comparing(UserVoteCount::getUserId));
    totalVoteCountsForPhrasesBeyondConsensus.sort(Comparator.comparing(UserVoteCount::getUserId));

    // assumes userIds line up
    List<UserVoteRatio> voteRatios = Streams.zip(completedVoteCounts.stream(), totalVoteCountsForPhrasesBeyondConsensus.stream(),
            (completed, total) -> new UserVoteRatio(completed.getUserId(), (double) completed.getVoteCount() / total.getVoteCount())).sorted(Comparator.comparing(UserVoteRatio::getVoteRatio).reversed()).collect(Collectors.toList());

      voteRatios.stream().map(UserVoteRatio::toString).forEach(log::info);

    return Response.ok().entity(voteRatios).build();
  }

}
