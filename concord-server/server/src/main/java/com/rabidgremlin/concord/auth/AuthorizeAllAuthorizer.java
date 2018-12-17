package com.rabidgremlin.concord.auth;

import io.dropwizard.auth.Authorizer;

/**
 * class that authorizes everyone.
 */
public class AuthorizeAllAuthorizer
    implements Authorizer<Caller>
{

  @Override
  public boolean authorize(Caller principal, String role)
  {
    return true;
  }

}
