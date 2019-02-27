package com.rabidgremlin.concord.dao;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import com.rabidgremlin.concord.api.Phrase;

public interface PhrasesDao
{

  @SqlUpdate("REPLACE INTO phrases(phraseId, text, completed, label, lastModified) VALUES (:phraseId, :text,:completed, NULL, CURRENT_TIMESTAMP)")
  void upsert(@Bind("phraseId") String phraseId, @Bind("text") String text, @Bind("completed") Boolean completed);

  @SqlBatch("REPLACE INTO phrases(phraseId, text, completed, lastModified) VALUES (:phraseId, :text,:completed, CURRENT_TIMESTAMP)")
  void upsertBatch(@Bind("phraseId") List<String> phraseIds, @Bind("text") List<String> phrases, @Bind("completed") Boolean completed);

  @SqlBatch("update phrases set completed = True, label = :label, completedTimestamp = CURRENT_TIMESTAMP, lastModified = CURRENT_TIMESTAMP where phraseId = :phraseId")
  void markPhrasesComplete(@Bind("phraseId") List<String> phraseId, @Bind("label") List<String> label);

  @SqlBatch("DELETE from phrases where phrases.phraseId = :phraseId")
  void deletePhrases(@Bind("phraseId") List<String> phraseId);

  @SqlQuery("select p.* from phrases p where p.phraseId not in (select v.phraseId from votes v where v.userId = :userId) AND p.completed = false limit 1")
  @RegisterBeanMapper(Phrase.class)
  Phrase getNextPhraseToLabelForUser(@Bind("userId") String userId);

  @SqlQuery("select p.phraseId from phrases p where p.completed = True")
  List<String> getCompletedPhraseIdentifiers();

}
