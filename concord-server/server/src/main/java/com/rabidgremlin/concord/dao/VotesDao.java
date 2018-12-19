package com.rabidgremlin.concord.dao;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface VotesDao
{
  @SqlUpdate("DELETE from votes where phraseId = :phraseId")
  void deleteAllVotesForPhrase(@Bind("phraseId") String phraseId);

  @SqlBatch("DELETE from votes where phraseId = :phraseId")
  void deleteAllVotesForPhrase(@Bind("phraseId") List<String> phraseId);

  @SqlUpdate("REPLACE INTO votes(phraseId, label, userId) VALUES (:phraseId, :label,:userId)")
  void upsert(@Bind("phraseId") String phraseId, @Bind("label") String label, @Bind("userId") String userId);

  @SqlQuery("select p.phraseId, v.label, p.text, COUNT(v.userId) AS voteCount from phrases p LEFT OUTER JOIN votes v on p.phraseId = v.phraseId WHERE p.completed = false GROUP BY p.phraseId, v.label, p.text HAVING voteCount > :margin")
  @RegisterBeanMapper(GroupedPhraseVote.class)
  List<GroupedPhraseVote> getPhraseVotesOverMargin(@Bind("margin") int margin);

  @SqlQuery("select p.phraseId, v.label, p.text, COUNT(v.userId) AS voteCount from phrases p LEFT OUTER JOIN votes v on p.phraseId = v.phraseId WHERE p.completed = false GROUP BY p.phraseId, v.label, p.text HAVING phraseId = :phraseId ORDER BY voteCount Desc limit 1 offset 1")
  @RegisterBeanMapper(GroupedPhraseVote.class)
  GroupedPhraseVote getSecondHighestContender(@Bind("phraseId") String phraseId);

}
