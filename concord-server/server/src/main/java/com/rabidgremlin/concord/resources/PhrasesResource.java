package com.rabidgremlin.concord.resources;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.rabidgremlin.concord.dao.UploadDao;
import com.rabidgremlin.concord.plugin.UnableToGetSuggestionsException;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;
import com.rabidgremlin.concord.api.Label;
import com.rabidgremlin.concord.api.Phrase;
import com.rabidgremlin.concord.api.PhraseToLabel;
import com.rabidgremlin.concord.api.PossibleLabel;
import com.rabidgremlin.concord.api.UnlabelledPhrase;
import com.rabidgremlin.concord.auth.Caller;
import com.rabidgremlin.concord.dao.PhraseVote;
import com.rabidgremlin.concord.dao.PhrasesDao;
import com.rabidgremlin.concord.dao.VotesDao;
import com.rabidgremlin.concord.plugin.LabelSuggester;
import com.rabidgremlin.concord.plugin.SuggestedLabel;

import io.dropwizard.auth.Auth;
import io.swagger.annotations.ApiParam;

@PermitAll
@Path("phrases")
@Produces(MediaType.APPLICATION_JSON)
public class PhrasesResource 
{
	@Context
    private UriInfo uriInfo;
	
	
	private Logger log = LoggerFactory.getLogger(PhrasesResource.class);
	private PhrasesDao phrasesDao;
	private VotesDao votesDao;
	private UploadDao uploadDao;
	private LabelSuggester labelSuggester;
	
	public PhrasesResource(PhrasesDao phrasesDao, VotesDao votesDao, UploadDao uploadDao, LabelSuggester labelSuggester)
	{
		this.phrasesDao = phrasesDao;
		this.votesDao = votesDao;
		this.uploadDao = uploadDao;
		this.labelSuggester = labelSuggester;
	}
	
	@GET
    @Timed
	@Path("/next")
	public Response getNextPhraseToLabel(@ApiParam(hidden = true) @Auth Caller caller)
	{
	   log.info("Caller {} getting next phrase to label",caller);
	   
	   Phrase nextPhrase = phrasesDao.getNextPhraseToLabelForUser(caller.getToken());	   
	   
	   if (nextPhrase == null)
	   {
		   return Response.status(Status.NOT_FOUND).build();
	   }


	   PhraseToLabel phraseToLabel = new PhraseToLabel();
	   phraseToLabel.setId(nextPhrase.getPhraseId());
	   phraseToLabel.setPhrase(nextPhrase.getText());
	   
	   
	   // TODO clean up mapping code, use beanutils and extract to util class etc
		try
		{
			List<SuggestedLabel> suggestedLabels = labelSuggester.suggestLabels(nextPhrase.getText());
			ArrayList<PossibleLabel> possibleLabels = new ArrayList<PossibleLabel>();

			for (SuggestedLabel suggestedLabel:suggestedLabels)
			{
				PossibleLabel tempLabel = new PossibleLabel();
				tempLabel.setLabel(suggestedLabel.getLabel());
				tempLabel.setLongDescription(suggestedLabel.getLongDescription());
				tempLabel.setScore(suggestedLabel.getScore());
				tempLabel.setShortDescription(suggestedLabel.getShortDescription());
				possibleLabels.add(tempLabel);
			}

			phraseToLabel.setPossibleLabels(possibleLabels);

			return Response.ok().entity(phraseToLabel).build();
		}
		catch (UnableToGetSuggestionsException e)
		{
			log.error("Failed to get suggestions", e);
			return Response.status(Status.SERVICE_UNAVAILABLE).build();
		}
	}
	
	
	
	@POST
	@Path("bulk")
    @Consumes("text/csv")
	@Timed
    public Response uploadCsv(@ApiParam(hidden = true) @Auth Caller caller, List<UnlabelledPhrase> unlabelledPhrases)
	{
		log.info("Caller {} uploading csv of phrases {}",caller, unlabelledPhrases);
		 
		uploadDao.uploadUnlabelledPhrases(unlabelledPhrases);

        return Response.ok().build();
    }
	
	@GET
	@Path("completed")
    @Produces("text/csv")
	@Timed
    public Response downloadCsv(@ApiParam(hidden = true) @Auth Caller caller) {
        
		 log.info("Caller {} downloading csv of completedPhrases {}",caller);
		 
		 
		 List<PhraseVote> phraseVotes = phrasesDao.getUncompletedPhraseVotes();
		 
		 // TODO mark as complete those downloaded
		 // TODO handle opposing votes. Need to only complete votes when clear majority for a particular label 
		
        
        return Response.ok().entity(phraseVotes).build();
    }
	
	@POST
    @Timed
	@Path("/{phraseId}/votes")
	// TODO: Create proper model class for incoming label
	public Response voteForPhrase(@ApiParam(hidden = true) @Auth Caller caller, @PathParam("phraseId") String phraseId, Label label)
	{
	   log.info("Caller {} casting vote for {}",caller, phraseId);
	   
	   votesDao.upsert(phraseId, label.getLabel(), caller.getToken());	  
	    
	   return Response.created(uriInfo.getAbsolutePath()).build();
	}
}
