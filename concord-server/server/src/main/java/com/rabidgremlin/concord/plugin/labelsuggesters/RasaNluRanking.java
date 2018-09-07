package com.rabidgremlin.concord.plugin.labelsuggesters;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RasaNluRanking{
	@JsonProperty
	public String name;
	@JsonProperty
	public Double confidence;
}
