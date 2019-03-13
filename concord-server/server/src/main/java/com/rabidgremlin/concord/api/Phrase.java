package com.rabidgremlin.concord.api;

import org.apache.commons.codec.digest.DigestUtils;

public class Phrase
{
  private String phraseId;

  private String text;

  private Boolean completed;

  private String label;

  public static String computePhraseId(String phrase)
  {
    return DigestUtils.md5Hex(phrase);
  }

  public Phrase(String text)
  {
    this.text = text;
    this.phraseId = computePhraseId(text);
  }

  public Phrase()
  {
  }

  public String getPhraseId()
  {
    return phraseId;
  }

  public void setPhraseId(String phraseId)
  {
    this.phraseId = phraseId;
  }

  public String getText()
  {
    return text;
  }

  public void setText(String text)
  {
    this.text = text;
  }

  public Boolean getCompleted()
  {
    return completed;
  }

  public void setCompleted(Boolean completed)
  {
    this.completed = completed;
  }

  public String getLabel()
  {
    return label;
  }

  public void setLabel(String label)
  {
    this.label = label;
  }

}
