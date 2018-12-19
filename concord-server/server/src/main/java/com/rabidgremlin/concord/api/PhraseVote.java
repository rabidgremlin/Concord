package com.rabidgremlin.concord.api;

public class PhraseVote
{

  private String phraseId;

  private String label;

  private String userId;

  public String getPhraseId()
  {
    return phraseId;
  }

  public void setPhraseId(String phraseId)
  {
    this.phraseId = phraseId;
  }

  public String getLabel()
  {
    return label;
  }

  public void setLabel(String label)
  {
    this.label = label;
  }

  public String getUserId()
  {
    return userId;
  }

  public void setUserId(String userId)
  {
    this.userId = userId;
  }
}
