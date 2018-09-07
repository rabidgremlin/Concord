package com.rabidgremlin.concord.plugin;

import java.util.HashMap;
import java.util.List;


public abstract class LabelSuggester 
{
	protected HashMap<String,Object> configProperties;
	protected SystemLabelStore systemLabelStore;
	
	public LabelSuggester(SystemLabelStore systemLabelStore)
	{
		configProperties = new HashMap<String,Object>();
		this.systemLabelStore = systemLabelStore;
	}
	
	public LabelSuggester(SystemLabelStore systemLabelStore, HashMap<String,Object> configProperties)
	{
		this.configProperties = configProperties;
		this.systemLabelStore = systemLabelStore;
	}
	
	public abstract List<SuggestedLabel> suggestLabels(String phrase);
}
