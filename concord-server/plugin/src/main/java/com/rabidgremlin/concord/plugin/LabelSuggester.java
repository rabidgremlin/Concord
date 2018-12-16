package com.rabidgremlin.concord.plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the label suggester for a plugin.
 * Responsible for returning a list of labels for a given phrase which users will then vote on.
 *
 * Example: {@link com.rabidgremlin.concord.plugin.labelsuggesters.RasaNluSuggester}
 * Example: {@link com.rabidgremlin.concord.plugin.labelsuggesters.AllLabelsSuggester}
 */
public abstract class LabelSuggester
{
	protected final Map<String,Object> configProperties;

	protected final SystemLabelStore systemLabelStore;
	
	public LabelSuggester(SystemLabelStore systemLabelStore)
	{
		configProperties = new HashMap<>();
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

	/**
	 * Return a list of suggested labels for the given phrase.
	 */
	public abstract List<SuggestedLabel> suggestLabels(String phrase) throws UnableToGetSuggestionsException;
}
