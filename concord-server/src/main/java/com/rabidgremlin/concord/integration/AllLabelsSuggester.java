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
	public List<PossibleLabel> suggestLabels(String phrase) 
	{
		List<Label> allLabels = labelsDao.getLabels();
		
		Double score = 1.0d / (double)allLabels.size();
		
		ArrayList<PossibleLabel> possibleLabels = new ArrayList<>();
		
		for(Label label: allLabels)
		{
			PossibleLabel tempLabel = new PossibleLabel();
			tempLabel.setLabel(label.getLabel());
			tempLabel.setShortDescription(label.getShortDescription());
			tempLabel.setLongDescription(label.getLongDescription());
			tempLabel.setScore(score);
			
			possibleLabels.add(tempLabel);
		}
		
		return possibleLabels;
	}

}
