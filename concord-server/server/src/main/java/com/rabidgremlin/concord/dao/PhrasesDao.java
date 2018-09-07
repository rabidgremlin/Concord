package com.rabidgremlin.concord.dao;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import com.rabidgremlin.concord.api.Label;
import com.rabidgremlin.concord.api.Phrase;

public interface PhrasesDao 
{	
	
	@SqlUpdate("REPLACE INTO phrases(phraseId, text, completed) VALUES (:phraseId, :text,:completed)")
    void upsert(@Bind("phraseId") String phraseId, @Bind("text") String text, @Bind("completed") Boolean completed);
	
	@SqlQuery("select p.* from phrases p LEFT OUTER JOIN votes v on p.phraseId = v.phraseId WHERE p.completed = false AND (v.userId IS NULL OR v.userId <> :userId) limit 1")
    @RegisterBeanMapper(Phrase.class)
    Phrase getNextPhraseToLabelForUser(@Bind("userId") String userId);
}
