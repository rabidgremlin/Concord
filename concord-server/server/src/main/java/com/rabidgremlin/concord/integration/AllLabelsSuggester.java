package com.rabidgremlin.concord.integration;

import java.util.ArrayList;
import java.util.List;

import com.rabidgremlin.concord.api.Label;
import com.rabidgremlin.concord.api.PossibleLabel;
import com.rabidgremlin.concord.dao.LabelsDao;

public class AllLabelsSuggester implements LabelSuggester 
{
	private LabelsDao labelsDao;

	public AllLabelsSuggester(LabelsDao labelsDao) 
	{		
		this.labelsDao = labelsDao;
	}

	@Override
	public List<SuggestedLabel> suggestLabels(String phrase) 
	{
		List<Label> allLabels = labelsDao.getLabels();
		
		Double score = 1.0d / (double)allLabels.size();
		
		ArrayList<SuggestedLabel> suggestedLabels = new ArrayList<>();
		
		for(Label label: allLabels)
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
