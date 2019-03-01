package com.rabidgremlin.concord.plugin;

public class UnableToGetSuggestionsException
    extends Exception
{
  public UnableToGetSuggestionsException(String errorMessage)
  {
    super(errorMessage);
  }
}
