package com.rabidgremlin.concord.dao;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import com.rabidgremlin.concord.api.Phrase;

public interface PhrasesDao 
{	
	
	@SqlUpdate("REPLACE INTO phrases(phraseId, text, completed, label) VALUES (:phraseId, :text,:completed, NULL)")
    void upsert(@Bind("phraseId") String phraseId, @Bind("text") String text, @Bind("completed") Boolean completed);
	
	@SqlQuery("select p.* from phrases p where p.phraseId not in (select v.phraseId from votes v where v.userId = :userId) limit 1")
    @RegisterBeanMapper(Phrase.class)
    Phrase getNextPhraseToLabelForUser(@Bind("userId") String userId);

	@SqlUpdate("update phrases set completed = :completed, label = :label where phraseId = :phraseId")
	void markPhrasesComplete(@Bind("phraseId") String phraseId, @Bind("completed") Boolean completed, @Bind("label") String label);
}
