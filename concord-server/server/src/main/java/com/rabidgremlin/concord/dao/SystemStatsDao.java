package com.rabidgremlin.concord.dao;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

public interface SystemStatsDao
{

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
      "ON p.phraseId = v.phraseId " +
      "WHERE completed = false")
  int getCountOfPhrasesWithConsensusThatAreNotCompleted(@Bind("consensusLevel") int consensusLevel);

}
