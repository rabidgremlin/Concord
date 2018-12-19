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
    // Add vote to phrases with user choice
    List<String> phraseIdsToVote = batchedPhrases.stream()
        .filter(unlabelledPhrase -> StringUtils.isNotEmpty(unlabelledPhrase.getPossibleLabel()))
        .map(unlabelledPhrase -> DigestUtils.md5Hex(unlabelledPhrase.getText()))
        .collect(Collectors.toList());

    votesDao().deleteAllVotesForPhrase(phraseIdsToVote);

    phrasesDao().upsertBatch(phraseIds, batchedPhrases.stream().map(UnlabelledPhrase::getText).collect(Collectors.toList()), false);

  }
}
