package com.rabidgremlin.concord.api;

import com.google.common.base.Objects;

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

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }
    LabelCount that = (LabelCount) o;
    return count == that.count &&
        Objects.equal(label, that.label);
  }

  @Override
  public int hashCode()
  {
    return Objects.hashCode(label, count);
  }

  @Override
  public String toString()
  {
    return "LabelCount{" +
        "label='" + label + '\'' +
        ", count=" + count +
        '}';
  }
}
