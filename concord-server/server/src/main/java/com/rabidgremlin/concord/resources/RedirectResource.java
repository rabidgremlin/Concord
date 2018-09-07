package com.rabidgremlin.concord.resources;

import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.logs.AWSLogs;
import com.amazonaws.services.logs.model.FilterLogEventsRequest;
import com.amazonaws.services.logs.model.FilterLogEventsResult;
import com.amazonaws.services.logs.model.FilteredLogEvent;
import com.codahale.metrics.annotation.Timed;


@PermitAll
@Path("/")
public class RedirectResource
{
  private Logger log = LoggerFactory.getLogger(RedirectResource.class);
  

  @GET
  @Timed
  public Response boing() throws URISyntaxException
  {
    return Response.temporaryRedirect(new URI("/ui/index.html")).build();
  }

}
