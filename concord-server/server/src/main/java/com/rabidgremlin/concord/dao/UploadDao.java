package com.rabidgremlin.concord.dao;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.jdbi.v3.sqlobject.CreateSqlObject;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import com.rabidgremlin.concord.api.UnlabelledPhrase;

public interface UploadDao
{

  @CreateSqlObject
  VotesDao votesDao();

  @CreateSqlObject
  PhrasesDao phrasesDao();

  @Transaction
  default void uploadUnlabelledPhrases(List<UnlabelledPhrase> unlabelledPhrases)
  {
    List<UnlabelledPhrase> batchedPhrases = unlabelledPhrases.stream()
        .filter(unlabelledPhrase -> !unlabelledPhrase.getText().equals("text"))
        .collect(Collectors.toList());

    List<String> phraseIds = batchedPhrases.stream()
        .map(unlabelledPhrase -> DigestUtils.md5Hex(unlabelledPhrase.getText()))
        .collect(Collectors.toList());

    // NOTE: Must delete votes before upsert, due to foreign key contraints
    votesDao().deleteAllVotesForPhrase(phraseIds);

    phrasesDao().upsertBatch(phraseIds, batchedPhrases.stream().map(UnlabelledPhrase::getText).collect(Collectors.toList()), false);

    // If there was a possible label, cast one vote for that intent
    for (UnlabelledPhrase phrase : batchedPhrases)
    {
      if (StringUtils.isNotEmpty(phrase.getPossibleLabel()))
      {
        votesDao().upsert(DigestUtils.md5Hex(phrase.getText()), phrase.getPossibleLabel(), "BULK_UPLOAD");
      }
    }
  }
}
