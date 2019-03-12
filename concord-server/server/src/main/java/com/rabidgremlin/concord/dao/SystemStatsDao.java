package com.rabidgremlin.concord.dao;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

public interface SystemStatsDao
{

  String USER_TO_IGNORE = "BULK_UPLOAD";

  @SqlQuery("SELECT COUNT(*) " +
      "FROM phrases")
  int getTotalCountOfPhrases();

  @SqlQuery("SELECT COUNT(*) " +
      "FROM phrases " +
      "WHERE completed = true")
  int getCountOfCompletedPhrases();

  @SqlQuery("SELECT COUNT(*) " +
      "FROM phrases p " +
      "JOIN(SELECT phraseId FROM votes GROUP BY phraseId HAVING COUNT(phraseId) >= :consensusLevel) v " +
      "ON p.phraseId = v.phraseId ")
  int getCountOfPhrasesWithConsensus(@Bind("consensusLevel") int consensusLevel);

  @SqlQuery("SELECT COUNT(*) " +
      "FROM phrases p " +
      "JOIN(SELECT phraseId FROM votes GROUP BY phraseId HAVING COUNT(phraseId) >= :consensusLevel) v " +
      "ON p.phraseId = v.phraseId " +
      "WHERE completed = false")
  int getCountOfPhrasesWithConsensusThatAreNotCompleted(@Bind("consensusLevel") int consensusLevel);

  @SqlQuery("SELECT COUNT(DISTINCT label) FROM phrases WHERE label IS NOT NULL")
  int getCountOfLabelsUsed();

  @SqlQuery("SELECT COUNT(*) " +
      "FROM votes " +
      "WHERE userId != '" + USER_TO_IGNORE + "'")
  int getCountOfVotes();

  @SqlQuery("SELECT COUNT(*) " +
      "FROM labels")
  int getCountOfLabels();

  @SqlQuery("SELECT COUNT(*) " +
      "FROM (SELECT userId FROM votes WHERE userId != '" + USER_TO_IGNORE + "' GROUP BY userId) ALIAS")
  int getCountOfUsers();

}
