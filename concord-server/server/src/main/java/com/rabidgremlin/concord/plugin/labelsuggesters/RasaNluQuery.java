package com.rabidgremlin.concord.plugin.labelsuggesters;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RasaNluQuery
{
	@JsonProperty
	public String q;
	@JsonProperty
	public String project;		
}