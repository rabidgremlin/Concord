package com.rabidgremlin.concord.dao;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import com.rabidgremlin.concord.api.UserVotesMade;

public interface VotesDao
{

  @SqlUpdate("DELETE from votes where phraseId = :phraseId")
  void deleteAllVotesForPhrase(@Bind("phraseId") String phraseId);

  @SqlBatch("DELETE from votes where phraseId = :phraseId")
  void deleteAllVotesForPhrase(@Bind("phraseId") List<String> phraseId);

  @SqlUpdate("REPLACE INTO votes(phraseId, label, userId, lastModified) VALUES (:phraseId, :label, :userId, CURRENT_TIMESTAMP)")
  void upsert(@Bind("phraseId") String phraseId, @Bind("label") String label, @Bind("userId") String userId);

  /**
   * This query returns incomplete phrases with the top 2 votes for each.
   * <P>
   * Note that there will be only one row for those only have one voted label.
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
      "    GROUP BY p.phraseId , p.text , v.label , v.label" +
      "    ORDER BY p.phraseId , p.text , voteCount DESC) AS t" +
      "    ) r" +
      " WHERE" +
      "    r.voteRank < 3" +
      "    and r.maxVote >= :margin")
  @RegisterBeanMapper(GroupedPhraseVote.class)
  List<GroupedPhraseVote> getPhraseOverMarginWithTop2Votes(@Bind("margin") int margin);

  @SqlQuery("select userId, count(*) votesMade from votes group by userId order by votesMade desc")
  @RegisterBeanMapper(UserVotesMade.class)
  List<UserVotesMade> getVotesMadePerUser();

}
