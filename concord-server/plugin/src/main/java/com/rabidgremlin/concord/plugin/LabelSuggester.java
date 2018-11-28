package com.rabidgremlin.concord.plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class LabelSuggester
{
	protected Map<String,Object> configProperties;
	protected SystemLabelStore systemLabelStore;
	
	public LabelSuggester(SystemLabelStore systemLabelStore)
	{
		configProperties = new HashMap<String,Object>();
		this.systemLabelStore = systemLabelStore;
	}
	
	public LabelSuggester(SystemLabelStore systemLabelStore, Map<String,Object> configProperties)
			throws InvalidConfigPropertiesException
	{
		if (configProperties.isEmpty())
		{
			throw new InvalidConfigPropertiesException("No configuration provided");
		}

		this.configProperties = configProperties;
		this.systemLabelStore = systemLabelStore;
	}
	
	public abstract List<SuggestedLabel> suggestLabels(String phrase) throws UnableToGetSuggestionsException;
}
