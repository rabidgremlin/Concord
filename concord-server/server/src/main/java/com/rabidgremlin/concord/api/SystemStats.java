package com.rabidgremlin.concord.api;

import java.util.List;

import com.google.common.base.Objects;

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

  private final List<LabelCountStats> labelCountStats;

  public SystemStats(int totalPhrases, int completedPhrases, int phrasesWithConsensus, int phrasesWithConsensusNotCompleted, int labelsUsed, int totalVotes,
    int totalLabels,
    int userCount, List<LabelCountStats> labelCountStats)
  {
    this.totalPhrases = totalPhrases;
    this.completedPhrases = completedPhrases;
    this.phrasesWithConsensus = phrasesWithConsensus;
    this.phrasesWithConsensusNotCompleted = phrasesWithConsensusNotCompleted;
    this.labelsUsed = labelsUsed;
    this.totalVotes = totalVotes;
    this.totalLabels = totalLabels;
    this.userCount = userCount;
    this.labelCountStats = labelCountStats;
  }

  public int getTotalPhrases()
  {
    return totalPhrases;
  }

  public int getCompletedPhrases()
  {
    return completedPhrases;
  }

  public int getPhrasesWithConsensus()
  {
    return phrasesWithConsensus;
  }

  public int getPhrasesWithConsensusNotCompleted()
  {
    return phrasesWithConsensusNotCompleted;
  }

  public int getLabelsUsed()
  {
    return labelsUsed;
  }

  public int getTotalVotes()
  {
    return totalVotes;
  }

  public int getTotalLabels()
  {
    return totalLabels;
  }

  public int getUserCount()
  {
    return userCount;
  }

  public List<LabelCountStats> getLabelCountStats()
  {
    return labelCountStats;
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
        phrasesWithConsensus == that.phrasesWithConsensus &&
        phrasesWithConsensusNotCompleted == that.phrasesWithConsensusNotCompleted &&
        labelsUsed == that.labelsUsed &&
        totalVotes == that.totalVotes &&
        totalLabels == that.totalLabels &&
        userCount == that.userCount &&
        Objects.equal(labelCountStats, that.labelCountStats);
  }

  @Override
  public int hashCode()
  {
    return Objects.hashCode(totalPhrases, completedPhrases, phrasesWithConsensus, phrasesWithConsensusNotCompleted, labelsUsed, totalVotes, totalLabels,
        userCount, labelCountStats);
  }

  @Override
  public String toString()
  {
    return "SystemStats{" +
        "totalPhrases=" + totalPhrases +
        ", completedPhrases=" + completedPhrases +
        ", phrasesWithConsensus=" + phrasesWithConsensus +
        ", phrasesWithConsensusNotCompleted=" + phrasesWithConsensusNotCompleted +
        ", labelsUsed=" + labelsUsed +
        ", totalVotes=" + totalVotes +
        ", totalLabels=" + totalLabels +
        ", userCount=" + userCount +
        ", labelCountStats=" + labelCountStats +
        '}';
  }

}
