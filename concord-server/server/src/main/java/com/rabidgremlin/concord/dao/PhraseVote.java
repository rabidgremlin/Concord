package com.rabidgremlin.concord.dao;

import io.github.binout.jaxrs.csv.CsvSchema;


@CsvSchema(separator=',',
columns = { "phraseId", "label", "text", "voteCount" })
public class PhraseVote {

	private String phraseId;
	private String label;
	private String text;
	private String voteCount;
	
	
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
	public String getVoteCount() {
		return voteCount;
	}
	public void setVoteCount(String voteCount) {
		this.voteCount = voteCount;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	
}
