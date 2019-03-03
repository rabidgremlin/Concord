package com.rabidgremlin.concord.resources;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;
import com.rabidgremlin.concord.api.UserVotesMade;
import com.rabidgremlin.concord.auth.Caller;
import com.rabidgremlin.concord.dao.VotesDao;

import io.dropwizard.auth.Auth;
import io.swagger.annotations.ApiParam;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource
{

  private static final String USER_TO_IGNORE = "BULK_UPLOAD";

  private final VotesDao votesDao;

  private final Logger log = LoggerFactory.getLogger(UsersResource.class);

  public UsersResource(VotesDao votesDao)
  {
    this.votesDao = votesDao;
  }

  @GET
  @Timed
  @Path("/scores")
  public Response getUserScores(@ApiParam(hidden = true) @Auth Caller caller)
  {
    log.info("Caller {} getting all user scores.", caller);

    List<UserVotesMade> userScores = votesDao.getVotesMadePerUser().stream()
        .filter(s -> !s.getUserId().equals(USER_TO_IGNORE))
        .collect(Collectors.toList());

    userScores.stream().map(UserVotesMade::toString).forEach(log::info);

    return Response.ok().entity(userScores).build();
  }

}
