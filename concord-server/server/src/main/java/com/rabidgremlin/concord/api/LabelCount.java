package com.rabidgremlin.concord.api;

public class LabelCount
{

  private String label;

  private int count;

  public LabelCount(String label, int count)
  {
    this.label = label;
    this.count = count;
  }

  public LabelCount()
  {
  }

  public String getLabel()
  {
    return label;
  }

  public void setLabel(String label)
  {
    this.label = label;
  }

  public int getCount()
  {
    return count;
  }

  public void setCount(int count)
  {
    this.count = count;
  }

}
