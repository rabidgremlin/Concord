package com.rabidgremlin.concord.resources;

import static java.util.Collections.singletonMap;
import static org.jose4j.jws.AlgorithmIdentifiers.HMAC_SHA256;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabidgremlin.concord.api.Error;
import com.rabidgremlin.concord.api.ErrorError;
import com.rabidgremlin.concord.api.NewSessionRequest;
import com.rabidgremlin.concord.plugin.CredentialsValidator;

@Path("/sessions")
@Produces(MediaType.APPLICATION_JSON)
public class SessionsResource
{
  private final Logger log = LoggerFactory.getLogger(SessionsResource.class);

  private final CredentialsValidator credentialsValidator;

  private final byte[] jwtTokenSecret;

  public SessionsResource(byte[] jwtTokenSecret, CredentialsValidator credentialsValidator)
  {
    super();
    this.jwtTokenSecret = jwtTokenSecret;
    this.credentialsValidator = credentialsValidator;
  }

  @POST
  public Response generateExpiredToken(NewSessionRequest newSessionRequest)
  {
    log.info("Generating token.");
    try
    {
      if (credentialsValidator.validateCredentials(newSessionRequest.getUserId(), newSessionRequest.getPassword()))
      {

        final JwtClaims claims = new JwtClaims();
        claims.setSubject(newSessionRequest.getUserId().toLowerCase());
        claims.setExpirationTimeMinutesInTheFuture(60);

        final JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue(HMAC_SHA256);
        jws.setKey(new HmacKey(jwtTokenSecret));

        return Response.ok().entity(singletonMap("token", jws.getCompactSerialization())).cookie(new NewCookie("JWT_COOKIE", jws.getCompactSerialization()))
            .build();
      }
      else
      {
        return Response.status(Status.UNAUTHORIZED).build();
      }
    }
    catch (JoseException e)
    {
      return Response.serverError().entity(new Error().error(new ErrorError().message(e.getMessage()))).build();
    }
  }

}
