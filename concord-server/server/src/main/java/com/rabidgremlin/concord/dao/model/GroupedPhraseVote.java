package com.rabidgremlin.concord.dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.github.binout.jaxrs.csv.CsvSchema;

@NoArgsConstructor
@AllArgsConstructor
@Data
@CsvSchema(separator = ',', columns = { "phraseId", "label", "text", "voteCount" })
public class GroupedPhraseVote
{

  private String phraseId;

  private String label;

  private String text;

  private int voteCount;

}
