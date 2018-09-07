package com.rabidgremlin.concord.integration;

import java.util.HashMap;
import java.util.List;


public abstract class LabelSuggester 
{
	protected HashMap<String,Object> configProperties;
	
	public LabelSuggester()
	{
		configProperties = new HashMap<String,Object>();
	}
	
	public LabelSuggester(HashMap<String,Object> configProperties)
	{
		this.configProperties = configProperties;
	}
	
	public abstract List<SuggestedLabel> suggestLabels(String phrase);
}
