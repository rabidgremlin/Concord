package com.rabidgremlin.concord.plugin.labelsuggesters;

import java.util.ArrayList;
import java.util.List;

import com.rabidgremlin.concord.api.Label;
import com.rabidgremlin.concord.api.PossibleLabel;
import com.rabidgremlin.concord.dao.LabelsDao;
import com.rabidgremlin.concord.plugin.LabelSuggester;
import com.rabidgremlin.concord.plugin.SuggestedLabel;
import com.rabidgremlin.concord.plugin.SystemLabel;
import com.rabidgremlin.concord.plugin.SystemLabelStore;

public class AllLabelsSuggester extends LabelSuggester 
{
	public AllLabelsSuggester(SystemLabelStore systemLabelStore) 
	{
		super(systemLabelStore);	
	}

	@Override
	public List<SuggestedLabel> suggestLabels(String phrase) 
	{
		// TODO: Maybe cache this look up for speed ?
		List<SystemLabel> allLabels = systemLabelStore.getSystemLabels();
		
		Double score = 1.0d / (double)allLabels.size();
		
		ArrayList<SuggestedLabel> suggestedLabels = new ArrayList<>();
		
		for(SystemLabel label: allLabels)
		{
			SuggestedLabel tempLabel = new SuggestedLabel();
			tempLabel.setLabel(label.getLabel());
			tempLabel.setShortDescription(label.getShortDescription());
			tempLabel.setLongDescription(label.getLongDescription());
			tempLabel.setScore(score);
			
			suggestedLabels.add(tempLabel);
		}
		
		return suggestedLabels;
	}

}