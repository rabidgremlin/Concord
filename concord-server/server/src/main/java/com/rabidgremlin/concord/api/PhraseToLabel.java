package com.rabidgremlin.concord.api;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PhraseToLabel
{
  private String id;

  private String phrase;

  private List<PossibleLabel> possibleLabels;

}
