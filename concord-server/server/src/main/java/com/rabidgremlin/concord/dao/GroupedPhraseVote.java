package com.rabidgremlin.concord.dao;

import io.github.binout.jaxrs.csv.CsvSchema;


@CsvSchema(separator=',',
columns = { "phraseId", "label", "text", "voteCount" })
public class GroupedPhraseVote {

	private String phraseId;
	private String label;
	private String text;
	private int voteCount;
	
	
	public String getPhraseId() {
		return phraseId;
	}
	public void setPhraseId(String phraseId) {
		this.phraseId = phraseId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getVoteCount() {
		return voteCount;
	}
	public void setVoteCount(int voteCount) {
		this.voteCount = voteCount;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	
}
