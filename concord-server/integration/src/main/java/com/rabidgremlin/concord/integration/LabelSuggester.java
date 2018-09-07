package com.rabidgremlin.concord.integration;

import java.util.List;


public interface LabelSuggester 
{
	List<SuggestedLabel> suggestLabels(String phrase);
}
