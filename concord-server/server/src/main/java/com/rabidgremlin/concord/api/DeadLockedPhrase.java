package com.rabidgremlin.concord.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DeadLockedPhrase
{

  private final Phrase phrase;

  private final List<LabelCount> labelsInVoteOrder;

  @JsonIgnore
  private final LocalDateTime mostRecentVoteTime;

}
