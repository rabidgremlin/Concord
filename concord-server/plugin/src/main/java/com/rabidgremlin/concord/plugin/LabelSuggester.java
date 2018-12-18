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

	/**
	 * Initialises the label suggester without config properties.
	 *
	 * @param systemLabelStore - store of all phrase labels.
	 */
	public LabelSuggester(SystemLabelStore systemLabelStore)
	{
		configProperties = new HashMap<>();
		this.systemLabelStore = systemLabelStore;
	}

	/**
	 * Initialises the label suggester with config properties (optional); e.g. a map of
	 * (api url -> authorisation token) pairs for your NLP model API.
	 *
	 * @param systemLabelStore - store of all phrase labels.
	 * @param configProperties - map of config properties; defined in server.yml
	 */
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
	 * Returns a list of suggested labels for the given phrase based labels stored in the systemLabelStore.
	 */
	public abstract List<SuggestedLabel> suggestLabels(String phrase) throws UnableToGetSuggestionsException;
}
