package com.rabidgremlin.concord.api;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DeadLockedPhrase
{

  private final Phrase phrase;

  private final LabelCount topLabel;

  private final LabelCount secondTopLabel;

  private final List<LabelCount> otherLabels;

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

}
