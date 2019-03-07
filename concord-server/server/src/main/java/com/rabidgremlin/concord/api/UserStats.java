package com.rabidgremlin.concord.api;

import com.google.common.base.Objects;

public class UserStats
{

  private final String userId;

  private final int totalVotes;

  private final int completedVotes;

  private final int trashVotes;

  private final int totalVotesWithConsensus;

  private final int completedVotesIgnoringTrash;

  private final int totalVotesWithConsensusIgnoringTrash;

  public UserStats(String userId, int totalVotes, int completedVotes, int trashVotes, int totalVotesWithConsensus, int completedVotesIgnoringTrash,
    int totalVotesWithConsensusIgnoringTrash)
  {
    this.userId = userId;
    this.totalVotes = totalVotes;
    this.completedVotes = completedVotes;
    this.trashVotes = trashVotes;
    this.totalVotesWithConsensus = totalVotesWithConsensus;
    this.completedVotesIgnoringTrash = completedVotesIgnoringTrash;
    this.totalVotesWithConsensusIgnoringTrash = totalVotesWithConsensusIgnoringTrash;
  }

  public String getUserId()
  {
    return userId;
  }

  public int getTotalVotes()
  {
    return totalVotes;
  }

  public int getCompletedVotes()
  {
    return completedVotes;
  }

  public int getTrashVotes()
  {
    return trashVotes;
  }

  public int getTotalVotesWithConsensus()
  {
    return totalVotesWithConsensus;
  }

  public int getCompletedVotesIgnoringTrash()
  {
    return completedVotesIgnoringTrash;
  }

  public int getTotalVotesWithConsensusIgnoringTrash()
  {
    return totalVotesWithConsensusIgnoringTrash;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }
    UserStats userStats = (UserStats) o;
    return totalVotes == userStats.totalVotes &&
        completedVotes == userStats.completedVotes &&
        trashVotes == userStats.trashVotes &&
        totalVotesWithConsensus == userStats.totalVotesWithConsensus &&
        completedVotesIgnoringTrash == userStats.completedVotesIgnoringTrash &&
        totalVotesWithConsensusIgnoringTrash == userStats.totalVotesWithConsensusIgnoringTrash &&
        Objects.equal(userId, userStats.userId);
  }

  @Override
  public int hashCode()
  {
    return Objects.hashCode(userId, totalVotes, completedVotes, trashVotes, totalVotesWithConsensus, completedVotesIgnoringTrash,
        totalVotesWithConsensusIgnoringTrash);
  }

  @Override
  public String toString()
  {
    return "UserStats{" +
        "userId='" + userId + '\'' +
        ", totalVotes=" + totalVotes +
        ", completedVotes=" + completedVotes +
        ", trashVotes=" + trashVotes +
        ", totalVotesWithConsensus=" + totalVotesWithConsensus +
        ", completedVotesIgnoringTrash=" + completedVotesIgnoringTrash +
        ", totalVotesWithConsensusIgnoringTrash=" + totalVotesWithConsensusIgnoringTrash +
        '}';
  }

}
