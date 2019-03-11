package com.rabidgremlin.concord.api;

import java.util.List;

public class UnlabelledPhrases
{

  private List<UnlabelledPhrase> unlabelledPhrases;

  public List<UnlabelledPhrase> getUnlabelledPhrases()
  {
    return unlabelledPhrases;
  }

  public void setUnlabelledPhrases(List<UnlabelledPhrase> unlabelledPhrases)
  {
    this.unlabelledPhrases = unlabelledPhrases;
  }

  @Override
  public String toString()
  {
    return "UnlabelledPhrases{" +
        "unlabelledPhrases=" + unlabelledPhrases +
        '}';
  }
}
