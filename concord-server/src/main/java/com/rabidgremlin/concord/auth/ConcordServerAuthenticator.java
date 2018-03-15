package com.rabidgremlin.concord.auth;

import java.util.Optional;

import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.JwtContext;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;

public class ConcordServerAuthenticator
    implements Authenticator<JwtContext, Caller>
{
  @Override
  public Optional<Caller> authenticate(JwtContext context)
    throws AuthenticationException
  {

    try
    {
      // TODO check that subject is a known user
      return Optional.of(new Caller(context.getJwtClaims().getSubject()));
    }
    catch (MalformedClaimException e)
    {
      // TODO logging
      return Optional.empty();
    }
  }
}
