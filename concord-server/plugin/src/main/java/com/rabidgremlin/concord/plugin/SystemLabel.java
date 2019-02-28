package com.rabidgremlin.concord.plugin;

/**
 * SystemLabel DAO
 */
public class SystemLabel 
{
	
	private String label;
	private String shortDescription;
	private String longDescription;
		
	public SystemLabel()
	{
		// do nothing
	}
	
	
	public SystemLabel(String label, String shortDescription,
			String longDescription) {		
		this.label = label;
		this.shortDescription = shortDescription;
		this.longDescription = longDescription;		
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


	@Override
	public String toString() {
		return "SystemLabel [label=" + label + ", shortDescription="
				+ shortDescription + ", longDescription=" + longDescription
				+ "]";
	}

}
