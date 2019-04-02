package com.rabidgremlin.concord.dao;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import com.rabidgremlin.concord.api.PhraseLabel;
import com.rabidgremlin.concord.dao.model.GroupedPhraseVote;
import com.rabidgremlin.concord.dao.model.GroupedPhraseVoteWithMostRecentVoteTime;

public interface VotesDao
{

  @SqlBatch("DELETE FROM votes WHERE phraseId = :phraseId")
  void deleteAllVotesForPhrase(@Bind("phraseId") List<String> phraseId);

  @SqlUpdate("REPLACE INTO votes(phraseId, label, userId, lastModifiedTimestamp) VALUES (:phraseId, :label, :userId, CURRENT_TIMESTAMP)")
  void upsert(@Bind("phraseId") String phraseId, @Bind("label") String label, @Bind("userId") String userId);

  @SqlQuery("SELECT phraseId, label FROM votes WHERE userId = :user")
  @RegisterBeanMapper(PhraseLabel.class)
  List<PhraseLabel> getVotesMadeByUser(@Bind("user") String user);

  /**
   * @return incomplete phrases with the top 2 vote labels for each. Note that there will be only one row for those who
   *         only have one voted label.
   */
  @SqlQuery("SELECT " +
      "    r.phraseId, r.text, r.label, r.voteCount" +
      " FROM" +
      "    (SELECT " +
      "        t.phraseId," +
      "            t.text," +
      "            t.label," +
      "            t.voteCount," +
      "            @voteRank:=IF(@current_phraseId = t.phraseId, @voteRank + 1, 1) AS voteRank," +
      "            @current_phraseId:=t.phraseId" +
      "    FROM" +
      // Must set user defined variables before they are used
      "        (SELECT @voteRank:= 1) vr," +
      "        (SELECT @current_phraseId:= '') cp," +
      "        (SELECT " +
      "        p.phraseId, p.text, v.label, COUNT(v.userId) AS voteCount" +
      "    FROM" +
      "        phrases p" +
      "    JOIN votes v ON p.phraseId = v.phraseId" +
      "    WHERE" +
      "        p.completed = FALSE" +
      "    GROUP BY p.phraseId, p.text, v.label" +
      "    ORDER BY p.phraseId , p.text , voteCount DESC) AS t" +
      "    ) AS r" +
      " WHERE" +
      "    r.voteRank <= 2")
  @RegisterBeanMapper(GroupedPhraseVote.class)
  List<GroupedPhraseVote> getTop2LabelsForUncompletedPhrases();

  /**
   * @return incomplete phrases with all vote labels for each. Note the most recent vote time is the same for each
   *         phrase label.
   */
  @SqlQuery("SELECT" +
      "    r.phraseId, r.text, r.label, r.voteCount, r.maxTime" +
      "    FROM" +
      "    (SELECT" +
      "        t.phraseId," +
      "            t.text," +
      "            t.label," +
      "            t.voteCount," +
      "            t.latestVoteTime," +
      "            @maxTime:=IF(@current_phraseId = t.phraseId, IF(@maxTime >= t.latestVoteTime, @maxTime, t.latestVoteTime), t.latestVoteTime) AS maxTime," +
      "            @current_phraseId:=t.phraseId" +
      "    FROM" +
      "        (SELECT" +
      "        p.phraseId, p.text, v.label, COUNT(v.userId) AS voteCount, MAX(v.lastModifiedTimestamp) AS latestVoteTime" +
      "    FROM" +
      "        phrases p" +
      "    JOIN votes v ON p.phraseId = v.phraseId" +
      "    WHERE" +
      "        p.completed = FALSE" +
      "    GROUP BY p.phraseId , p.text , v.label) AS t" +
      "    ) r")
  @RegisterBeanMapper(GroupedPhraseVoteWithMostRecentVoteTime.class)
  List<GroupedPhraseVoteWithMostRecentVoteTime> getLabelsForUncompletedPhrasesWithMostRecentVoteTime();

  @SqlQuery("SELECT COUNT(*) FROM votes WHERE phraseId = :phraseId")
  int getVoteCountForPhrase(@Bind("phraseId") String phraseId);

}
