package com.rabidgremlin.concord.dao;

import java.sql.Timestamp;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

public interface SystemStatsDao
{

  String FILTER_USERS = "WHERE userId != 'BULK_UPLOAD' AND userId != 'RESOLVER'";

  @SqlQuery("SELECT COUNT(*) FROM phrases")
  int getTotalCountOfPhrases();

  @SqlQuery("SELECT COUNT(*) FROM phrases WHERE completed = true AND label != 'TRASH'")
  int getCountOfCompletedPhrases();

  @SqlQuery("SELECT COUNT(DISTINCT label) FROM phrases WHERE label IS NOT NULL")
  int getCountOfLabelsUsed();

  @SqlQuery("SELECT COUNT(*) FROM votes " + FILTER_USERS)
  int getCountOfVotes();

  @SqlQuery("SELECT COUNT(*) FROM labels")
  int getCountOfLabels();

  @SqlQuery("SELECT COUNT(DISTINCT userId) FROM votes " + FILTER_USERS + " AND lastModifiedTimestamp > :earliestVoteTime")
  int getCountOfActiveUsers(@Bind("earliestVoteTime") Timestamp earliestVoteTime);

}
