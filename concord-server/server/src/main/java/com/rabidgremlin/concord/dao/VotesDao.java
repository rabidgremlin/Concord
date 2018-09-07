package com.rabidgremlin.concord.dao;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface VotesDao 
{
	@SqlUpdate("DELETE from votes where phraseId = :phraseId")
	void deleteAllVotesForPhrase(@Bind("phraseId") String phraseId);
	
	@SqlUpdate("REPLACE INTO votes(phraseId, label, userId) VALUES (:phraseId, :label,:userId)")
	void upsert(@Bind("phraseId") String phraseId, @Bind("label") String label, @Bind("userId") String userId);
		
}
