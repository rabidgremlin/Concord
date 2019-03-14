package com.rabidgremlin.concord.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.ImmutableList;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Data
public class DeadLockedPhrase
{

  private final Phrase phrase;

  private final List<LabelCount> labelsInVoteOrder;

  @JsonIgnore
  private final LocalDateTime mostRecentVoteTime;

  public DeadLockedPhrase(Phrase phrase, List<LabelCount> labelsInVoteOrder, LocalDateTime mostRecentVoteTime)
  {
    this.phrase = phrase;
    labelsInVoteOrder.sort(Comparator.comparingInt(LabelCount::getCount).reversed());
    this.labelsInVoteOrder = ImmutableList.copyOf(labelsInVoteOrder);
    this.mostRecentVoteTime = mostRecentVoteTime;
  }

}
