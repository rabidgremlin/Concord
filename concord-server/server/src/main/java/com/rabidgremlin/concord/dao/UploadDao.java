package com.rabidgremlin.concord.dao;

import com.rabidgremlin.concord.api.UnlabelledPhrase;
import org.jdbi.v3.sqlobject.CreateSqlObject;
import org.jdbi.v3.sqlobject.transaction.Transaction;

public interface UploadDao {

    @CreateSqlObject
    VotesDao votesDao();

    @CreateSqlObject
    PhrasesDao phrasesDao();

    @Transaction
    default void deleteExistingVotesAndUploadPhrase(String phraseId, UnlabelledPhrase unlabelledPhrase) {
        votesDao().deleteAllVotesForPhrase(phraseId);
        phrasesDao().upsert(phraseId, unlabelledPhrase.getText(), false);
    }
}


