package com.rabidgremlin.concord.integration;

import java.util.List;

import com.rabidgremlin.concord.api.PossibleLabel;

public interface LabelSuggester 
{
	List<PossibleLabel> suggestLabels(String phrase);
}
