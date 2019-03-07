package com.rabidgremlin.concord.resources;

import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.codahale.metrics.annotation.Timed;

@PermitAll
@Path("/")
public class RedirectResource
{

  @GET
  @Timed
  public Response boing()
    throws URISyntaxException
  {
    return Response.temporaryRedirect(new URI("/ui/index.html")).build();
  }

}
