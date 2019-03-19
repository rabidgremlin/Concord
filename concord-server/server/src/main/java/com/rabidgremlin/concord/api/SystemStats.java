package com.rabidgremlin.concord.api;

import java.util.List;

import lombok.Data;

@Data
public class SystemStats
{

  private final int totalPhrases;

  private final int completedPhrases;

  private final int labelsUsed;

  private final int totalVotes;

  private final int totalLabels;

  private final int userCount;

  private final int consensusLevel;

  private final List<DeadLockedPhrase> deadLockedPhrases;

}
