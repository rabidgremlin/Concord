package com.rabidgremlin.concord.auth;

import java.security.Principal;

/**
 * The caller to the api which will be authenticated.
 */
public class Caller
    implements Principal
{
  private final String token;

  public Caller(String token)
  {
    this.token = token;
  }

  @Override
  public String getName()
  {
    return token;
  }

  public String getToken()
  {
    return token;
  }

  @Override
  public String toString()
  {
    return "Caller [token=" + token + "]";
  }

}
