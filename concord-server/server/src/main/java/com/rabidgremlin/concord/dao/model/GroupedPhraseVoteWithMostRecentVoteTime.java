package com.rabidgremlin.concord.dao.model;

import io.github.binout.jaxrs.csv.CsvSchema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@CsvSchema(separator = ',', columns = { "phraseId", "label", "text", "voteCount, maxTime" })
public class GroupedPhraseVoteWithMostRecentVoteTime
    extends GroupedPhraseVote
{

  private Timestamp maxTime;

  public GroupedPhraseVoteWithMostRecentVoteTime(String label, String text, int voteCount, Timestamp maxTime)
  {
    super(label, text, voteCount);
    this.maxTime = maxTime;
  }

}
