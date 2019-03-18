package com.rabidgremlin.concord.api;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
public class DeadLockedPhrase
{

  private final Phrase phrase;

  private final List<LabelCount> labelsInVoteOrder;

  @JsonIgnore
  private final LocalDateTime mostRecentVoteTime;

}
