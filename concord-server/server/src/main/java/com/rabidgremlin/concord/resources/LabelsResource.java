package com.rabidgremlin.concord.resources;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;
import com.rabidgremlin.concord.api.Label;
import com.rabidgremlin.concord.api.PhraseToLabel;
import com.rabidgremlin.concord.auth.Caller;
import com.rabidgremlin.concord.dao.LabelsDao;

import io.dropwizard.auth.Auth;
import io.swagger.annotations.ApiParam;

@PermitAll
@Path("labels")
@Produces(MediaType.APPLICATION_JSON)
public class LabelsResource 
{
	@Context
    private UriInfo uriInfo;
	
	private Logger log = LoggerFactory.getLogger(LabelsResource.class);
	private LabelsDao labelsDao;
	
	public LabelsResource(LabelsDao labelsDao)
	{
		this.labelsDao = labelsDao;
	}
	
	
	
	@POST
    @Timed	
    @Consumes(MediaType.APPLICATION_JSON)	
	public Response upsert(@ApiParam(hidden = true) @Auth Caller caller, Label label)
	{
	   log.info("Caller {} adding new label {}",caller, label);

	   labelsDao.upsert(label);
	    
	   return Response.created(uriInfo.getAbsolutePath()).build();
	}
	
	
	@GET
    @Timed	
	public Response getLabels(@ApiParam(hidden = true) @Auth Caller caller)
	{
	   log.info("Caller {} getting next phrase to label",caller);

	   List<Label> labels = labelsDao.getLabels();
	    
	   return Response.ok().entity(labels).build();
	}
	
	@POST
	@Path("bulk")
    @Consumes("text/csv")
    public Response uploadCsv(@ApiParam(hidden = true) @Auth Caller caller, List<Label> labels) {
        
		 log.info("Caller {} uploading csv of labels {}",caller, labels);
		 
		 // TODO Transaction...
		 labelsDao.deleteAllLabels();

		 for(Label label:labels)
		 {
		   // skip header	 
		   if (label.getLabel().equals("label")){
			   continue;
		   }
		   labelsDao.upsert(label);
		 }
		
        
        return Response.ok().build();
    }
}