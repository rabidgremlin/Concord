package com.rabidgremlin.concord.plugin.labelsuggesters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RasaNluResponse
{

  private RasaNluRanking[] intentRanking;

  public RasaNluRanking[] getIntentRanking()
  {
    return intentRanking;
  }

  @JsonProperty("intent_ranking")
  public void setIntentRanking(RasaNluRanking[] intentRanking)
  {
    this.intentRanking = intentRanking;
  }

}
