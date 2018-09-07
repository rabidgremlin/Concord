package com.rabidgremlin.concord.plugin.labelsuggesters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rabidgremlin.concord.api.Label;
import com.rabidgremlin.concord.api.PossibleLabel;
import com.rabidgremlin.concord.dao.LabelsDao;
import com.rabidgremlin.concord.plugin.LabelSuggester;
import com.rabidgremlin.concord.plugin.SuggestedLabel;
import com.rabidgremlin.concord.plugin.SystemLabel;
import com.rabidgremlin.concord.plugin.SystemLabelStore;

public class RasaNluSuggester extends LabelSuggester 
{
	
	
	
	
	
	
	
	
	
	
	
	private RasaNluService rasaNluService;
	
	public RasaNluSuggester(SystemLabelStore systemLabelStore) 
	{
		super(systemLabelStore);	
		
		Retrofit retrofit = new Retrofit.Builder()
	    .baseUrl("http://localhost:5000")
	    .addConverterFactory(JacksonConverterFactory.create())
	    .build();
		
		rasaNluService =retrofit.create(RasaNluService.class);
	}

	@Override
	public List<SuggestedLabel> suggestLabels(String phrase) 
	{
		// TODO: Maybe cache this look up for speed ?
		List<SystemLabel> systemLabels = systemLabelStore.getSystemLabels();		
		HashMap<String,SystemLabel> systemLabelsMap = new HashMap<String,SystemLabel>();		
		
		for (SystemLabel systemLabel:systemLabels)
		{
			systemLabelsMap.put(systemLabel.getLabel().toLowerCase(), systemLabel);
		}
		
		
		RasaNluQuery query = new RasaNluQuery();
		query.q = phrase;
		query.project = "taxibot";
		Response<RasaNluResponse> response;
		
		try {
			response = rasaNluService.getSuggestions(query).execute();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		
		ArrayList<SuggestedLabel> suggestedLabels = new ArrayList<>();
		
		for( RasaNluRanking ranking :response.body().getIntentRanking())
		{
			SuggestedLabel tempLabel = new SuggestedLabel();
			tempLabel.setLabel(ranking.name);			
			tempLabel.setScore(ranking.confidence);
			
			SystemLabel matchingSystemLable = systemLabelsMap.get(ranking.name.toLowerCase());
			if (matchingSystemLable != null)
			{
				tempLabel.setLongDescription(matchingSystemLable.getLongDescription());
				tempLabel.setShortDescription(matchingSystemLable.getShortDescription());
			}
			
			suggestedLabels.add(tempLabel);
		}
		
		return suggestedLabels;
	}

}