package com.rabidgremlin.concord.dao;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jdbi.v3.sqlobject.CreateSqlObject;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import com.rabidgremlin.concord.api.Phrase;
import com.rabidgremlin.concord.api.UnlabelledPhrase;

public interface UploadDao
{

  @CreateSqlObject
  VotesDao votesDao();

  @CreateSqlObject
  PhrasesDao phrasesDao();

  enum EXISTING_VOTES
  {
    REPLACE, RETAIN
  }

  default void uploadUnlabelledPhrases(List<UnlabelledPhrase> unlabelledPhrases, EXISTING_VOTES voteContract)
  {
    List<UnlabelledPhrase> batchedPhrases = unlabelledPhrases.stream()
        .filter(unlabelledPhrase -> !unlabelledPhrase.getText().equalsIgnoreCase("text"))
        .collect(Collectors.toList());

    List<String> phraseIds = batchedPhrases.stream()
        .map(unlabelledPhrase -> Phrase.computePhraseId(unlabelledPhrase.getText()))
        .collect(Collectors.toList());

    if (voteContract.equals(EXISTING_VOTES.REPLACE))
    {
      uploadUnlabelledPhrasesWithReplacement(batchedPhrases, phraseIds);
    }
    else
    {
      uploadUnlabelledPhrasesWithoutReplacement(batchedPhrases, phraseIds);
    }
  }

  @Transaction
  default void uploadUnlabelledPhrasesWithReplacement(List<UnlabelledPhrase> batchedPhrases, List<String> phraseIds)
  {
    // NOTE: Must delete votes before upsert, due to foreign key contraints
    votesDao().deleteAllVotesForPhrase(phraseIds);

    phrasesDao().upsertBatch(phraseIds, batchedPhrases.stream()
        .map(UnlabelledPhrase::getText)
        .collect(Collectors.toList()), false);

    // If there was a possible label, cast one vote for that intent
    batchedPhrases.stream()
        .filter(phrase -> StringUtils.isNotEmpty(phrase.getPossibleLabel()))
        .forEach(phrase -> votesDao().upsert(Phrase.computePhraseId(phrase.getText()), phrase.getPossibleLabel(), "BULK_UPLOAD"));
  }

  @Transaction
  default void uploadUnlabelledPhrasesWithoutReplacement(List<UnlabelledPhrase> batchedPhrases, List<String> phraseIds)
  {
    phrasesDao().insertBatch(phraseIds, batchedPhrases.stream()
        .map(UnlabelledPhrase::getText)
        .collect(Collectors.toList()), false);

    // If there was a possible label, cast one vote for that intent - if a vote doesn't already exist
    batchedPhrases.stream()
        .filter(phrase -> StringUtils.isNotEmpty(phrase.getPossibleLabel()))
        .forEach(phrase -> votesDao().insert(Phrase.computePhraseId(phrase.getText()), phrase.getPossibleLabel(), "BULK_UPLOAD"));
  }
}
