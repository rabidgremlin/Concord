package com.rabidgremlin.concord.resources;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;
import com.rabidgremlin.concord.api.Label;
import com.rabidgremlin.concord.api.PhraseToLabel;
import com.rabidgremlin.concord.api.UnlabelledPhrase;
import com.rabidgremlin.concord.auth.Caller;
import com.rabidgremlin.concord.dao.LabelsDao;
import com.rabidgremlin.concord.dao.PhrasesDao;

import io.dropwizard.auth.Auth;
import io.swagger.annotations.ApiParam;

@PermitAll
@Path("phrases")
@Produces(MediaType.APPLICATION_JSON)
public class PhrasesResource 
{
	
	private Logger log = LoggerFactory.getLogger(PhrasesResource.class);
	private PhrasesDao phrasesDao;
	
	public PhrasesResource(PhrasesDao phrasesDao)
	{
		this.phrasesDao = phrasesDao;
	}
	
	
	@GET
    @Timed
	@Path("/next")
	public Response getNextPhraseToLabel(@ApiParam(hidden = true) @Auth Caller caller)
	{
	   log.info("Caller {} getting next phrase to label",caller);

	   PhraseToLabel phraseToLabel = new PhraseToLabel();
	   phraseToLabel.setId("0dd09ff3-ea8d-4438-b45b-39e49d8b2aca");
	   phraseToLabel.setPhrase("This is a test phrase");
	    
	   return Response.ok().entity(phraseToLabel).build();
	}
	
	
	
	@POST
	@Path("bulk")
    @Consumes("text/csv")
    public Response uploadCsv(@ApiParam(hidden = true) @Auth Caller caller, List<UnlabelledPhrase> unlabelledPhrases) {
        
		 log.info("Caller {} uploading csv of labels {}",caller, unlabelledPhrases);
		 
		 
		 for(UnlabelledPhrase unlabelledPhrase:unlabelledPhrases)
		 {
		   // skip header	 
		   if (unlabelledPhrase.getText().equals("text")){
			   continue;
		   }
		   //labelsDao.upsert(label);
		   
		   String phraseId = DigestUtils.md5Hex(unlabelledPhrase.getText());
		   
		   phrasesDao.insert(phraseId, unlabelledPhrase.getText(), false);
		 }
		
        
        return Response.ok().build();
    }
}
