package com.rabidgremlin.concord.plugin;

/**
 * SuggestedLabel DAO
 */
public class SuggestedLabel
{

  private String label;

  private String shortDescription;

  private String longDescription;

  private Double score;

  public SuggestedLabel()
  {
    // do nothing
  }

  public SuggestedLabel(String label, String shortDescription,
    String longDescription, Double score)
  {
    this.label = label;
    this.shortDescription = shortDescription;
    this.longDescription = longDescription;
    this.score = score;
  }

  public String getLabel()
  {
    return label;
  }

  public void setLabel(String label)
  {
    this.label = label;
  }

  public String getShortDescription()
  {
    return shortDescription;
  }

  public void setShortDescription(String shortDescription)
  {
    this.shortDescription = shortDescription;
  }

  public String getLongDescription()
  {
    return longDescription;
  }

  public void setLongDescription(String longDescription)
  {
    this.longDescription = longDescription;
  }

  public Double getScore()
  {
    return score;
  }

  public void setScore(Double score)
  {
    this.score = score;
  }

  @Override
  public String toString()
  {
    return "SuggestedLabel [label=" + label + ", shortDescription="
        + shortDescription + ", longDescription=" + longDescription
        + ", score=" + score + "]";
  }

}
