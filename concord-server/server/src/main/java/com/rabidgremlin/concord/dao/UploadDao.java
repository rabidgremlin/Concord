package com.rabidgremlin.concord.dao;

import com.rabidgremlin.concord.api.UnlabelledPhrase;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.jdbi.v3.sqlobject.CreateSqlObject;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.util.List;

public interface UploadDao {

    @CreateSqlObject
    VotesDao votesDao();

    @CreateSqlObject
    PhrasesDao phrasesDao();

    @Transaction
    default void uploadUnlabelledPhrases(List<UnlabelledPhrase> unlabelledPhrases) {

        for(UnlabelledPhrase unlabelledPhrase : unlabelledPhrases)
        {
            // skip header
            if (unlabelledPhrase.getText().equals("text")){
                continue;
            }

            String phraseId = DigestUtils.md5Hex(unlabelledPhrase.getText());

            // remove any existing votes in case we are reloading data
            votesDao().deleteAllVotesForPhrase(phraseId);

            // User choice counts as one vote
            if (StringUtils.isNotEmpty(unlabelledPhrase.getPossibleLabel()))
            {
                votesDao().upsert(phraseId, unlabelledPhrase.getPossibleLabel(), "User");
            }

            phrasesDao().upsert(phraseId, unlabelledPhrase.getText(), false);
        }
    }
}


