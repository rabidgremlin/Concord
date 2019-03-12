package com.rabidgremlin.concord.api;

import java.util.List;

import com.google.common.base.Objects;
import com.rabidgremlin.concord.util.PhraseUtil;

public class DeadLockedPhrase
{

  private final String phrase;

  private final String phraseId;

  private final LabelCount topLabel;

  private final LabelCount secondTopLabel;

  private final List<LabelCount> otherLabels;

  public DeadLockedPhrase(String phrase, LabelCount topLabel, LabelCount secondTopLabel, List<LabelCount> otherLabels)
  {
    this.phrase = phrase;
    this.phraseId = PhraseUtil.computePhraseId(phrase);
    this.topLabel = topLabel;
    this.secondTopLabel = secondTopLabel;
    this.otherLabels = otherLabels;
  }

  public String getPhrase()
  {
    return phrase;
  }

  public String getPhraseId()
  {
    return phraseId;
  }

  public LabelCount getTopLabel()
  {
    return topLabel;
  }

  public LabelCount getSecondTopLabel()
  {
    return secondTopLabel;
  }

  public int voteSum()
  {
    return topLabel.getCount() + secondTopLabel.getCount() + otherLabels.stream().mapToInt(LabelCount::getCount).sum();
  }

  public int voteDifference()
  {
    return topLabel.getCount() - secondTopLabel.getCount();
  }

  public boolean isDeadLocked(int userCount, int consensusLevel)
  {
    // TODO, better to show more phrases? Could do something like active users this week
    return voteDifference() > userCount - voteSum() - consensusLevel + 1;
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
    DeadLockedPhrase that = (DeadLockedPhrase) o;
    return Objects.equal(phrase, that.phrase) &&
        Objects.equal(topLabel, that.topLabel) &&
        Objects.equal(secondTopLabel, that.secondTopLabel);
  }

  @Override
  public int hashCode()
  {
    return Objects.hashCode(phrase, topLabel, secondTopLabel);
  }

  @Override
  public String toString()
  {
    return "DeadLockedPhrase{" +
        "phrase='" + phrase + '\'' +
        ", topLabel=" + topLabel +
        ", secondTopLabel=" + secondTopLabel +
        '}';
  }
}
