package com.rabidgremlin.concord.api;

import com.google.common.base.Objects;

public class SystemStats
{

  private final int totalPhrases;

  private final int completedPhrases;

  private final int phrasesWithConsensusNotCompleted;

  public SystemStats(int totalPhrases, int completedPhrases, int phrasesWithConsensusNotCompleted)
  {
    this.totalPhrases = totalPhrases;
    this.completedPhrases = completedPhrases;
    this.phrasesWithConsensusNotCompleted = phrasesWithConsensusNotCompleted;
  }

  public int getTotalPhrases()
  {
    return totalPhrases;
  }

  public int getCompletedPhrases()
  {
    return completedPhrases;
  }

  public int getPhrasesWithConsensusNotCompleted()
  {
    return phrasesWithConsensusNotCompleted;
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
    SystemStats that = (SystemStats) o;
    return totalPhrases == that.totalPhrases &&
        completedPhrases == that.completedPhrases &&
        phrasesWithConsensusNotCompleted == that.phrasesWithConsensusNotCompleted;
  }

  @Override
  public int hashCode()
  {
    return Objects.hashCode(totalPhrases, completedPhrases, phrasesWithConsensusNotCompleted);
  }

  @Override
  public String toString()
  {
    return "SystemStats{" +
        "totalPhrases=" + totalPhrases +
        ", completedPhrases=" + completedPhrases +
        ", phrasesWithConsensusNotCompleted=" + phrasesWithConsensusNotCompleted +
        '}';
  }

}
