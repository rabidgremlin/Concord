package com.rabidgremlin.concord.api;

import com.google.common.base.Objects;

public class LabelCountStats
{

  private final String label;

  private final int voteCount;

  private final int completedPhraseCount;

  public LabelCountStats(String label, int voteCount, int completedPhraseCount)
  {
    this.label = label;
    this.voteCount = voteCount;
    this.completedPhraseCount = completedPhraseCount;
  }

  public String getLabel()
  {
    return label;
  }

  public int getVoteCount()
  {
    return voteCount;
  }

  public int getCompletedPhraseCount()
  {
    return completedPhraseCount;
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
    LabelCountStats that = (LabelCountStats) o;
    return Objects.equal(label, that.label) &&
        Objects.equal(voteCount, that.voteCount) &&
        Objects.equal(completedPhraseCount, that.completedPhraseCount);
  }

  @Override
  public int hashCode()
  {
    return Objects.hashCode(label, voteCount, completedPhraseCount);
  }

  @Override
  public String toString()
  {
    return "LabelCountStats{" +
        "label='" + label + '\'' +
        ", voteCount='" + voteCount + '\'' +
        ", completedPhraseCount='" + completedPhraseCount + '\'' +
        '}';
  }

}
