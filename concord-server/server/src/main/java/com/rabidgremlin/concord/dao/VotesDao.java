package com.rabidgremlin.concord.dao;

import java.util.List;
import java.util.Set;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import com.rabidgremlin.concord.dao.model.GroupedPhraseVote;
import com.rabidgremlin.concord.dao.model.GroupedPhraseVoteWithMostRecentVoteTime;

public interface VotesDao
{

  @SqlBatch("DELETE from votes where phraseId = :phraseId")
  void deleteAllVotesForPhrase(@Bind("phraseId") List<String> phraseId);

  @SqlUpdate("REPLACE INTO votes(phraseId, label, userId, lastModifiedTimestamp) VALUES (:phraseId, :label, :userId, CURRENT_TIMESTAMP)")
  void upsert(@Bind("phraseId") String phraseId, @Bind("label") String label, @Bind("userId") String userId);

  @SqlQuery("SELECT phraseId FROM votes WHERE userId = :user")
  Set<String> getPhraseIdsForVotesMadeByUser(@Bind("user") String user);

  /**
   * This query returns incomplete phrases with the top 2 votes for each. Note that there will be only one row for those
   * only have one voted label.
   *
   * @param margin consensus level required for a phrase to be considered complete
   */
  @SqlQuery("SELECT " +
      "    r.phraseId, r.text, r.label, r.voteCount, r.voteRank" +
      " FROM" +
      "    (SELECT " +
      "        t.phraseId," +
      "            t.text," +
      "            t.label," +
      "            t.voteCount," +
      "            @voteRank:=IF(@current_phraseId = t.phraseId, @voteRank + 1, 1) AS voteRank," +
      "            @maxVote:=IF(@current_phraseId = t.phraseId, IF(@maxVote >= t.voteCount,@maxVote,t.voteCount), t.voteCount) AS maxVote," +
      "            @current_phraseId:=t.phraseId" +
      "    FROM" +
      "        (SELECT " +
      "        p.phraseId, p.text, v.label, COUNT(v.userId) AS voteCount" +
      "    FROM" +
      "        phrases p" +
      "    JOIN votes v ON p.phraseId = v.phraseId" +
      "    WHERE" +
      "        p.completed = FALSE" +
      "    GROUP BY p.phraseId , p.text , v.label" +
      "    ORDER BY p.phraseId , p.text , voteCount DESC) AS t" +
      "    ) r" +
      " WHERE" +
      "    r.voteRank < 3" +
      "    and r.maxVote >= :margin")
  @RegisterBeanMapper(GroupedPhraseVote.class)
  List<GroupedPhraseVote> getTop2LabelsForUncompletedPhrasesOverMarginInVoteCountOrder(@Bind("margin") int margin);

  @SqlQuery("SELECT" +
      "    r.phraseId, r.text, r.label, r.voteCount, r.voteRank, r.maxTime" +
      "    FROM" +
      "    (SELECT" +
      "        t.phraseId," +
      "            t.text," +
      "            t.label," +
      "            t.voteCount," +
      "            t.latestVoteTime," +
      "            @voteRank:=IF(@current_phraseId = t.phraseId, @voteRank + 1, 1) AS voteRank," +
      "            @maxVote:=IF(@current_phraseId = t.phraseId, IF(@maxVote >= t.voteCount,@maxVote,t.voteCount), t.voteCount) AS maxVote," +
      "            @maxTime:=IF(@current_phraseId = t.phraseId, IF(@maxTime >= t.latestVoteTime,@maxTime,t.latestVoteTime), t.latestVoteTime) AS maxTime," +
      "            @current_phraseId:=t.phraseId" +
      "    FROM" +
      "        (SELECT" +
      "        p.phraseId, p.text, v.label, COUNT(v.userId) AS voteCount, max(v.lastModifiedTimestamp) as latestVoteTime" +
      "    FROM" +
      "        phrases p" +
      "    JOIN votes v ON p.phraseId = v.phraseId" +
      "    WHERE" +
      "        p.completed = FALSE" +
      "    GROUP BY p.phraseId , p.text , v.label) AS t" +
      "    ) r")
  @RegisterBeanMapper(GroupedPhraseVoteWithMostRecentVoteTime.class)
  List<GroupedPhraseVoteWithMostRecentVoteTime> getLabelsForUncompletedPhrasesInVoteCountOrder();

}
