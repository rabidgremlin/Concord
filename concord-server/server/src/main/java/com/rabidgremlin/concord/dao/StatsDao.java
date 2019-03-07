package com.rabidgremlin.concord.dao;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import com.rabidgremlin.concord.api.UserVoteCount;

/*
 * NOTE: not filtering SKIPPED we check against the consensus
 */
public interface StatsDao
{

  /**
   * Returns the raw total count of votes made per user.
   */
  @SqlQuery("SELECT userId, COUNT(*) voteCount " +
      "FROM votes " +
      "GROUP BY userId ")
  @RegisterBeanMapper(UserVoteCount.class)
  List<UserVoteCount> getCountOfTotalVotesPerUser();

  @SqlQuery("SELECT userId, COUNT(*) voteCount " +
      "FROM votes " +
      "WHERE label = 'TRASH' " +
      "GROUP BY userId ")
  @RegisterBeanMapper(UserVoteCount.class)
  List<UserVoteCount> getCountOfTrashVotesPerUser();

  /**
   * Returns the count of votes made per user, for phrases which have been marked complete with the same label the user
   * voted for.
   */
  @SqlQuery("SELECT userId, COUNT(*) voteCount " +
      "FROM votes v " +
      "JOIN(SELECT phraseId, label FROM phrases WHERE completed = true) p ON v.phraseId = p.phraseId " +
      "WHERE v.label = p.label " +
      "GROUP BY userId ")
  @RegisterBeanMapper(UserVoteCount.class)
  List<UserVoteCount> getCountOfCompletedVotesPerUser();

  @SqlQuery("SELECT userId, COUNT(*) voteCount " +
      "FROM votes v " +
      "JOIN(SELECT phraseId, label FROM phrases WHERE completed = true AND label != 'TRASH') p on v.phraseId = p.phraseId " +
      "WHERE p.label = v.label " +
      "GROUP BY userId ")
  @RegisterBeanMapper(UserVoteCount.class)
  List<UserVoteCount> getCountOfCompletedVotesPerUserIgnoringTrash();

  /**
   * Returns the count of votes made per user, for phrases which have been marked complete (reached consensus).
   */
  @SqlQuery("SELECT userId, COUNT(*) voteCount " +
      "FROM votes v " +
      "JOIN(SELECT phraseId FROM phrases WHERE completed = true) p ON v.phraseId = p.phraseId " +
      "GROUP BY userId ")
  @RegisterBeanMapper(UserVoteCount.class)
  List<UserVoteCount> getCountOfTotalVotesWithConsensusPerUser();

  @SqlQuery("SELECT userId, COUNT(*) voteCount " +
      "FROM votes v " +
      "JOIN(SELECT phraseId FROM phrases WHERE completed = true AND label != 'TRASH') p ON v.phraseId = p.phraseId " +
      "WHERE label != 'TRASH' " + // must check votes.label too since votes.label could be different to phrases.label
      "GROUP BY userId ")
  @RegisterBeanMapper(UserVoteCount.class)
  List<UserVoteCount> getCountOfTotalVotesWithConsensusPerUserIgnoringTrash();

}
