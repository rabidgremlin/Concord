package com.rabidgremlin.concord.resources;

import com.codahale.metrics.annotation.Timed;
import com.rabidgremlin.concord.api.Label;
import com.rabidgremlin.concord.api.UserVotesMade;
import com.rabidgremlin.concord.auth.Caller;
import com.rabidgremlin.concord.dao.VotesDao;
import io.dropwizard.auth.Auth;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("votes")
@Produces(MediaType.APPLICATION_JSON)
public class VotesResource
{

    private final VotesDao votesDao;

    private final Logger log = LoggerFactory.getLogger(VotesResource.class);

    public VotesResource(VotesDao votesDao)
    {
        this.votesDao = votesDao;
    }

    @GET
    @Timed
    @Path("/scores")
    public Response getUserScores()
    {
        log.info("Getting user scores");

        List<UserVotesMade> userScores = votesDao.getVotesMadePerUser();

        return Response.ok().entity(userScores).build();
    }

}
