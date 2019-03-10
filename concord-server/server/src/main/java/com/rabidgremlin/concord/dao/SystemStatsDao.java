package com.rabidgremlin.concord.dao;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import com.rabidgremlin.concord.api.LabelCount;

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

  @SqlQuery("SELECT COUNT(*) " +
      "FROM (SELECT label FROM phrases WHERE label IS NOT NULL GROUP BY label) ALIAS")
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

  @SqlQuery("SELECT label FROM labels")
  List<String> getLabelNames();

  @SqlQuery("SELECT label, COUNT(*) count " +
      "FROM phrases " +
      "WHERE label is not NULL AND completed = true " +
      "GROUP BY label")
  @RegisterBeanMapper(LabelCount.class)
  List<LabelCount> getCompletedPhraseLabelCounts();

  @SqlQuery("SELECT label, COUNT(*) count FROM votes GROUP BY label")
  @RegisterBeanMapper(LabelCount.class)
  List<LabelCount> getLabelVoteCounts();

}
