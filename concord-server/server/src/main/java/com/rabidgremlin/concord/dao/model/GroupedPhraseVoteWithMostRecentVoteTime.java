package com.rabidgremlin.concord.dao.model;

import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import io.github.binout.jaxrs.csv.CsvSchema;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@CsvSchema(separator = ',', columns = { "phraseId", "label", "text", "voteCount, maxTime" })
public class GroupedPhraseVoteWithMostRecentVoteTime
    extends GroupedPhraseVote
{

  public GroupedPhraseVoteWithMostRecentVoteTime(String label, String text, int voteCount, Timestamp maxTime)
  {
    super(label, text, voteCount);
    this.maxTime = maxTime;
  }

  private Timestamp maxTime;

}
