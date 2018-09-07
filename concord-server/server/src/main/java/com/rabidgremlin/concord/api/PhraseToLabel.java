package com.rabidgremlin.concord.api;

import java.util.List;

public class PhraseToLabel 
{
	private String id;
	private String phrase;
	private List<PossibleLabel> possibleLabels;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPhrase() {
		return phrase;
	}
	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}
	public List<PossibleLabel> getPossibleLabels() {
		return possibleLabels;
	}
	public void setPossibleLabels(List<PossibleLabel> possibleLabels) {
		this.possibleLabels = possibleLabels;
	}

    
	
}
