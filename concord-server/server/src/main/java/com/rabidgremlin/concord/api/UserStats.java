package com.rabidgremlin.concord.api;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
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
