package com.rabidgremlin.concord.resources;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;
import com.rabidgremlin.concord.api.DeadLockedPhrase;
import com.rabidgremlin.concord.api.LabelCount;
import com.rabidgremlin.concord.api.SystemStats;
import com.rabidgremlin.concord.api.UserStats;
import com.rabidgremlin.concord.api.UserVoteCount;
import com.rabidgremlin.concord.auth.Caller;
import com.rabidgremlin.concord.dao.GroupedPhraseVote;
import com.rabidgremlin.concord.dao.SystemStatsDao;
import com.rabidgremlin.concord.dao.UserStatsDao;
import com.rabidgremlin.concord.dao.VotesDao;

import io.dropwizard.auth.Auth;
import io.swagger.annotations.ApiParam;

@PermitAll
@Path("stats")
@Produces(MediaType.APPLICATION_JSON)
public class StatsResource
{

  private final UserStatsDao userStatsDao;

  private final SystemStatsDao systemStatsDao;

  private final VotesDao votesDao;

  private final int consensusLevel;

  private final Logger log = LoggerFactory.getLogger(StatsResource.class);

  public StatsResource(UserStatsDao userStatsDao, SystemStatsDao systemStatsDao, VotesDao votesDao, int consensusLevel)
  {
    this.userStatsDao = userStatsDao;
    this.systemStatsDao = systemStatsDao;
    this.votesDao = votesDao;
    this.consensusLevel = consensusLevel;
  }

  @GET
  @Timed
  @Path("/user")
  public Response getUserStats(@ApiParam(hidden = true) @Auth Caller caller)
  {
    log.info("{} getting user stats.", caller);

    List<UserVoteCount> totalVoteCounts = userStatsDao.getCountOfTotalVotesPerUser();
    List<UserVoteCount> completedVoteCounts = userStatsDao.getCountOfCompletedVotesPerUser();
    List<UserVoteCount> trashedVoteCounts = userStatsDao.getCountOfTrashVotesPerUser();
    List<UserVoteCount> totalVoteCountsForPhrasesWithConsensus = userStatsDao.getCountOfTotalVotesWithConsensusPerUser();
    List<UserVoteCount> completedVoteCountsIgnoringTrash = userStatsDao.getCountOfCompletedVotesPerUserIgnoringTrash();
    List<UserVoteCount> totalVotesForPhrasesWithConsensusIgnoringTrash = userStatsDao
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
        .collect(toList());

    return Response.ok().entity(userStats).build();
  }

  private int getVotesForUser(List<UserVoteCount> list, String userId)
  {
    return list.stream()
        .filter(userVoteCount -> userVoteCount.getUserId().equals(userId))
        .findFirst()
        .map(UserVoteCount::getVoteCount)
        .orElse(0);
  }

  @GET
  @Timed
  @Path("/system")
  public Response getSystemStats(@ApiParam(hidden = true) @Auth Caller caller)
  {
    log.info("{} getting system stats.", caller);

    int totalPhrases = systemStatsDao.getTotalCountOfPhrases();
    int completedPhrases = systemStatsDao.getCountOfCompletedPhrases();
    int phrasesWithConsensus = systemStatsDao.getCountOfPhrasesWithConsensus(consensusLevel);
    int phrasesWithConsensusNotCompleted = systemStatsDao.getCountOfPhrasesWithConsensusThatAreNotCompleted(consensusLevel);
    int labelsUsed = systemStatsDao.getCountOfLabelsUsed();
    int totalVotes = systemStatsDao.getCountOfVotes();
    int totalLabels = systemStatsDao.getCountOfLabels();
    int userCount = systemStatsDao.getCountOfUsers();

    List<GroupedPhraseVote> uncompletedPhraseVotesWithConsensus = votesDao.getPhraseOverMarginWithTop2Votes(consensusLevel);
    List<DeadLockedPhrase> deadLockedPhrases = uncompletedPhraseVotesWithConsensus.stream()
        .collect(
            groupingBy(GroupedPhraseVote::getText,
                mapping(phrase -> new LabelCount(phrase.getLabel(), phrase.getVoteCount()), toList())))
        .entrySet()
        .stream()
        // only care about phrases that have votes for multiple labels
        .filter((e) -> e.getValue().size() >= 2)
        .map((e) -> {
          // sort by labels with most votes
          List<LabelCount> labels = e.getValue().stream().sorted(comparingInt(LabelCount::getCount).reversed()).collect(toList());
          LabelCount topLabel = labels.get(0);
          LabelCount secondTopLabel = labels.get(1);
          List<LabelCount> otherLabels = labels.size() >= 3 ? labels.subList(2, labels.size()) : Collections.emptyList();
          return new DeadLockedPhrase(e.getKey(), topLabel, secondTopLabel, otherLabels);
        })
        .filter(phrase -> phrase.isDeadLocked(userCount, consensusLevel))
        // sort by phrases with most votes
        .sorted(comparingInt(DeadLockedPhrase::voteSum).reversed())
        .collect(toList());

    SystemStats systemStats = new SystemStats(totalPhrases, completedPhrases, phrasesWithConsensus, phrasesWithConsensusNotCompleted, labelsUsed, totalVotes,
        totalLabels, userCount, deadLockedPhrases);

    return Response.ok().entity(systemStats).build();
  }

}
