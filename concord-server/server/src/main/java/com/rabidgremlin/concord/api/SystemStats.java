package com.rabidgremlin.concord.api;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SystemStats
{

  private final int totalPhrases;

  private final int completedPhrases;

  private final int phrasesWithConsensus;

  private final int phrasesWithConsensusNotCompleted;

  private final int labelsUsed;

  private final int totalVotes;

  private final int totalLabels;

  private final int userCount;

  private final List<DeadLockedPhrase> deadLockedPhrases;

}
