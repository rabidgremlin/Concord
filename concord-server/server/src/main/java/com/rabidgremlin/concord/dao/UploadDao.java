package com.rabidgremlin.concord.dao;

import com.rabidgremlin.concord.api.UnlabelledPhrase;
import org.apache.commons.codec.digest.DigestUtils;
import org.jdbi.v3.sqlobject.CreateSqlObject;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.util.List;

public interface UploadDao {

    @CreateSqlObject
    VotesDao votesDao();

    @CreateSqlObject
    PhrasesDao phrasesDao();

    @CreateSqlObject
    LabelsDao labelsDao();

    @Transaction
    default void uploadUnlabelledPhrases(List<UnlabelledPhrase> unlabelledPhrases) {

        for(UnlabelledPhrase unlabelledPhrase : unlabelledPhrases)
        {
            // skip header
            if (unlabelledPhrase.getText().equals("text")){
                continue;
            }
            //labelsDao().upsert(unlabelledPhrase.getPossibleLabel());

            String phraseId = DigestUtils.md5Hex(unlabelledPhrase.getText());

            // remove any existing votes in case we are reloading data
            votesDao().deleteAllVotesForPhrase(phraseId);
            phrasesDao().upsert(phraseId, unlabelledPhrase.getText(), false);
        }

    }
}


