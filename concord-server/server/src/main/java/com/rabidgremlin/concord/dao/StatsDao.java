package com.rabidgremlin.concord.dao;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import com.rabidgremlin.concord.api.UserVoteCount;

public interface StatsDao
{

  /**
   * Returns the raw total count of votes made per user.
   */
  @SqlQuery("SELECT userId, COUNT(*) voteCount " +
      "FROM votes " +
      "GROUP BY userId " +
      "ORDER BY voteCount DESC")
  @RegisterBeanMapper(UserVoteCount.class)
  List<UserVoteCount> getTotalCountOfVotesMadePerUser();

  /*
   * NOTE: not filtering SKIPPED we check against the consensus
   */

  /**
   * Returns the count of votes made per user, for phrases which have been marked complete with the same label the user
   * voted for.
   */
  @SqlQuery("SELECT userId, COUNT(*) voteCount " +
      "FROM votes " +
      "JOIN phrases ON phrases.phraseId=votes.phraseId " +
      "WHERE completed = true AND phrases.label=votes.label " +
      "GROUP BY userId " +
      "ORDER BY voteCount DESC")
  @RegisterBeanMapper(UserVoteCount.class)
  List<UserVoteCount> getCompletedCountOfVotesMadePerUser();

  @SqlQuery("SELECT userId, COUNT(*) voteCount " +
      "FROM votes " +
      "JOIN phrases ON phrases.phraseId=votes.phraseId " +
      "WHERE completed = true AND phrases.label=votes.label " +
      "AND votes.label != 'TRASH' " +
      "GROUP BY userId " +
      "ORDER BY voteCount DESC")
  @RegisterBeanMapper(UserVoteCount.class)
  List<UserVoteCount> getCompletedCountOfVotesMadePerUserIgnoringTrash();

  /**
   * Returns the total count of votes made per user, for phrases which have been voted on more or equal times of the
   * margin.
   */
  @SqlQuery("SELECT userId, COUNT(*) voteCount " +
      "FROM votes v " +
      "JOIN(SELECT phraseId FROM phrases WHERE completed = true) p ON v.phraseId = p.phraseId " +
      "GROUP BY userId " +
      "ORDER BY voteCount DESC")
  @RegisterBeanMapper(UserVoteCount.class)
  List<UserVoteCount> getTotalCountOfVotesMadePerUserWithConsensus();

  @SqlQuery("SELECT userId, COUNT(*) voteCount " +
      "FROM votes v " +
      "JOIN(SELECT phraseId FROM phrases WHERE label != 'TRASH' AND completed = true) p ON v.phraseId = p.phraseId " +
      "WHERE label != 'TRASH' " +
      "GROUP BY userId " +
      "ORDER BY voteCount DESC")
  @RegisterBeanMapper(UserVoteCount.class)
  List<UserVoteCount> getTotalCountOfVotesMadePerUsersWithConsensusIgnoringTrash();

  @SqlQuery("SELECT userId, COUNT(*) voteCount " +
      "FROM votes " +
      "WHERE label = 'TRASH' " +
      "GROUP BY userId " +
      "ORDER BY voteCount DESC")
  @RegisterBeanMapper(UserVoteCount.class)
  List<UserVoteCount> getCountOfTrashVotesPerUser();

}
