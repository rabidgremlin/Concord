package com.rabidgremlin.concord.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;
import com.rabidgremlin.concord.api.UserVotesMade;
import com.rabidgremlin.concord.dao.VotesDao;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource
{

  private final VotesDao votesDao;

  private final Logger log = LoggerFactory.getLogger(UsersResource.class);

  public UsersResource(VotesDao votesDao)
  {
    this.votesDao = votesDao;
  }

  @GET
  @Timed
  @Path("/scores")
  public Response getUserScores()
  {
    log.info("Getting all users scores.");

    List<UserVotesMade> userScores = votesDao.getVotesMadePerUser();

    userScores.stream().map(UserVotesMade::toString).forEach(log::info);

    return Response.ok().entity(userScores).build();
  }

}