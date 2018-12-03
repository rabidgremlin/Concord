package com.rabidgremlin.concord.api;

import io.github.binout.jaxrs.csv.CsvSchema;


@CsvSchema(separator=',', columns = { "label", "shortDescription", "longDescription" })
public class Label
{
	private String label;
	private String shortDescription;
	private String longDescription;
	

	public Label(String label, String shortDescription, String longDescription)
	{
		this.label = label;
		this.shortDescription = shortDescription;
		this.longDescription = longDescription;
	}

	public Label()
	{

	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}
	

}
