package com.rabidgremlin.concord.dao.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.rabidgremlin.concord.api.Phrase;
import io.github.binout.jaxrs.csv.CsvSchema;

@NoArgsConstructor
@Data
@CsvSchema(separator = ',', columns = { "phraseId", "label", "text", "voteCount" })
public class GroupedPhraseVote
{

  private String phraseId;

  private String label;

  private String text;

  private int voteCount;

  public GroupedPhraseVote(String label, String text, int voteCount)
  {
    this.label = label;
    this.text = text;
    this.voteCount = voteCount;
    this.phraseId = Phrase.computePhraseId(text);
  }
}
