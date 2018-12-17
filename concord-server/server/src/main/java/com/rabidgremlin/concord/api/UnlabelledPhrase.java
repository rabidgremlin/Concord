package com.rabidgremlin.concord.api;

import io.github.binout.jaxrs.csv.CsvSchema;

@CsvSchema(separator = ',', columns = { "text", "possibleLabel" })
public class UnlabelledPhrase
{
  private String text;

  private String possibleLabel;

  public String getText()
  {
    return text;
  }

  public void setText(String text)
  {
    this.text = text;
  }

  public String getPossibleLabel()
  {
    return possibleLabel;
  }

  public void setPossibleLabel(String possibleLabel)
  {
    this.possibleLabel = possibleLabel;
  }

}
