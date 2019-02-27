package com.rabidgremlin.concord.resources;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;
import com.rabidgremlin.concord.api.Label;
import com.rabidgremlin.concord.api.Phrase;
import com.rabidgremlin.concord.api.PhraseToLabel;
import com.rabidgremlin.concord.api.PossibleLabel;
import com.rabidgremlin.concord.api.UnlabelledPhrase;
import com.rabidgremlin.concord.auth.Caller;
import com.rabidgremlin.concord.dao.GroupedPhraseVote;
import com.rabidgremlin.concord.dao.PhrasesDao;
import com.rabidgremlin.concord.dao.UploadDao;
import com.rabidgremlin.concord.dao.VotesDao;
import com.rabidgremlin.concord.functions.GetEligiblePhrasesForCompletion;
import com.rabidgremlin.concord.plugin.LabelSuggester;
import com.rabidgremlin.concord.plugin.SuggestedLabel;
import com.rabidgremlin.concord.plugin.UnableToGetSuggestionsException;

import io.dropwizard.auth.Auth;
import io.swagger.annotations.ApiParam;

@PermitAll
@Path("phrases")
@Produces(MediaType.APPLICATION_JSON)
public class PhrasesResource
{
  @Context
  protected UriInfo uriInfo;

  private Logger log = LoggerFactory.getLogger(PhrasesResource.class);

  private PhrasesDao phrasesDao;

  private VotesDao votesDao;

  private UploadDao uploadDao;

  private LabelSuggester labelSuggester;

  private int consensusLevel;

  private boolean completeOnTrash;

  private final static String LABEL_TRASH = "TRASH";

  public PhrasesResource(PhrasesDao phrasesDao, VotesDao votesDao, UploadDao uploadDao, LabelSuggester labelSuggester, int consensusLevel,
      boolean completeOnTrash)
  {
    this.phrasesDao = phrasesDao;
    this.votesDao = votesDao;
    this.uploadDao = uploadDao;
    this.labelSuggester = labelSuggester;
    this.consensusLevel = consensusLevel;
    this.completeOnTrash = completeOnTrash;
  }

  @GET
  @Timed
  @Path("/next")
  public Response getNextPhraseToLabel(@ApiParam(hidden = true) @Auth Caller caller)
  {
    log.info("Caller {} getting next phrase to label", caller);

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
      ArrayList<PossibleLabel> possibleLabels = new ArrayList<>();

      for (SuggestedLabel suggestedLabel : suggestedLabels)
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
    log.info("Caller {} uploading csv of phrases {}", caller, unlabelledPhrases);

    uploadDao.uploadUnlabelledPhrases(unlabelledPhrases);

    return Response.ok().build();
  }

  @GET
  @Path("completed")
  @Produces("text/csv")
  @Timed
  public synchronized Response downloadCsv(@ApiParam(hidden = true) @Auth Caller caller)
  {

    log.info("Caller {} marking phrases and downloading csv of completedPhrases", caller);

    log.info("Searching for votes over margin {}...", consensusLevel);
    List<GroupedPhraseVote> phraseVotes = votesDao.getPhraseOverMarginWithTop2Votes(consensusLevel);
    log.info("Found {} votes over margin.", phraseVotes.size());

    GetEligiblePhrasesForCompletion getPhrases = new GetEligiblePhrasesForCompletion(phraseVotes, consensusLevel);

    log.info("Looking for completed phrases...");
    List<Phrase> completedPhrases = getPhrases.execute();
    log.info("Found {} completed phrases.", completedPhrases.size());

    log.info("Marking phrases complete...");
    phrasesDao.markPhrasesComplete(completedPhrases.stream().map(Phrase::getPhraseId).collect(Collectors.toList()),
        completedPhrases.stream().map(Phrase::getLabel).collect(Collectors.toList()));
    log.info("Marking phrases complete done.");

    return Response.ok().entity(completedPhrases).build();
  }

  @DELETE
  @Path("completed")
  @Timed
  public Response purgeCompletedPhrasesAndVotes(@ApiParam(hidden = true) @Auth Caller caller)
  {

    log.info("Caller {} purging completed votes and phrases {}", caller);

    List<String> phraseIdentifiers = phrasesDao.getCompletedPhraseIdentifiers();
    int amount = phraseIdentifiers.size();

    votesDao.deleteAllVotesForPhrase(phraseIdentifiers);
    phrasesDao.deleteCompletedPhrases(phraseIdentifiers);

    log.info(amount + " phrases purged from database.");

    return Response.ok().build();
  }

  @POST
  @Timed
  @Path("/{phraseId}/votes")
  // TODO: Create proper model class for incoming label
  public Response voteForPhrase(@ApiParam(hidden = true) @Auth Caller caller, @PathParam("phraseId") String phraseId, Label label)
  {
    log.info("Caller {} casting vote for {}", caller, phraseId);

    votesDao.upsert(phraseId, label.getLabel(), caller.getToken());

    // are we marking trashed labels as completed?
    if (completeOnTrash && StringUtils.equals(LABEL_TRASH, label.getLabel()))
    {
      phrasesDao.markPhrasesComplete(Arrays.asList(phraseId), Arrays.asList(label.getLabel()));
    }

    return Response.created(uriInfo.getAbsolutePath()).build();
  }
}
