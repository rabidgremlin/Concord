package com.rabidgremlin.concord.dao;

import com.rabidgremlin.concord.api.Phrase;
import com.rabidgremlin.concord.api.UnlabelledPhrase;
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
  default void uploadUnlabelledPhrases(List<UnlabelledPhrase> unlabelledPhrases)
  {
    List<UnlabelledPhrase> batchedPhrases = unlabelledPhrases.stream()
        .filter(unlabelledPhrase -> !unlabelledPhrase.getText().equals("text"))
        .collect(Collectors.toList());

    List<String> phraseIds = batchedPhrases.stream()
        .map(unlabelledPhrase -> Phrase.computePhraseId(unlabelledPhrase.getText()))
        .collect(Collectors.toList());

    // NOTE: Must delete votes before upsert, due to foreign key contraints
    votesDao().deleteAllVotesForPhrase(phraseIds);

    phrasesDao().upsertBatch(phraseIds, batchedPhrases.stream().map(UnlabelledPhrase::getText).collect(Collectors.toList()), false);

    // If there was a possible label, cast one vote for that intent
    for (UnlabelledPhrase phrase : batchedPhrases)
    {
      if (StringUtils.isNotEmpty(phrase.getPossibleLabel()))
      {
        votesDao().upsert(Phrase.computePhraseId(phrase.getText()), phrase.getPossibleLabel(), "BULK_UPLOAD");
      }
    }
  }
}
