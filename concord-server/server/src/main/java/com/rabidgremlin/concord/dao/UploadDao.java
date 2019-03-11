package com.rabidgremlin.concord.dao;

import com.rabidgremlin.concord.api.UnlabelledPhrase;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.jdbi.v3.sqlobject.CreateSqlObject;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.util.List;
import java.util.stream.Collectors;

public interface UploadDao
{

  @CreateSqlObject
  VotesDao votesDao();

  @CreateSqlObject
  PhrasesDao phrasesDao();

  @Transaction
  default void uploadUnlabelledPhrases(String userId, List<UnlabelledPhrase> unlabelledPhrases)
  {
    List<UnlabelledPhrase> batchedPhrases = unlabelledPhrases.stream()
        .filter(unlabelledPhrase -> !unlabelledPhrase.getText().equalsIgnoreCase("text"))
        .collect(Collectors.toList());

    List<String> phraseIds = batchedPhrases.stream()
        .map(unlabelledPhrase -> DigestUtils.md5Hex(unlabelledPhrase.getText()))
        .collect(Collectors.toList());

    // NOTE: Must delete votes before upsert, due to foreign key contraints
    votesDao().deleteAllVotesForPhrase(phraseIds);

    phrasesDao().upsertBatch(phraseIds, batchedPhrases.stream()
        .map(UnlabelledPhrase::getText)
        .collect(Collectors.toList()), false);

    // If there was a possible label, cast one vote for that phrase label
    batchedPhrases.stream()
        .filter(phrase -> StringUtils.isNotEmpty(phrase.getPossibleLabel()))
        .forEach(phrase -> votesDao().upsert(DigestUtils.md5Hex(phrase.getText()),
            phrase.getPossibleLabel(), StringUtils.isNotEmpty(userId) ? userId : "BULK_UPLOAD"));
  }

}
