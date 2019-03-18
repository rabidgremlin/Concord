package com.rabidgremlin.concord.api;

import lombok.Data;

@Data
public class UserStats
{

  private final String userId;

  private final int totalVotes;

  private final int completedVotes;

  private final int trashVotes;

  private final int totalVotesWithConsensus;

  private final int completedVotesIgnoringTrash;

  private final int totalVotesWithConsensusIgnoringTrash;

}
