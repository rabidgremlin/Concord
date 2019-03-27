package com.rabidgremlin.concord.resources;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import com.rabidgremlin.concord.api.PhraseLabel;
import com.rabidgremlin.concord.api.PhraseToLabel;
import com.rabidgremlin.concord.api.PossibleLabel;
import com.rabidgremlin.concord.api.UnlabelledPhrase;
import com.rabidgremlin.concord.api.UnlabelledPhrases;
import com.rabidgremlin.concord.auth.Caller;
import com.rabidgremlin.concord.dao.PhrasesDao;
import com.rabidgremlin.concord.dao.UploadDao;
import com.rabidgremlin.concord.dao.VotesDao;
import com.rabidgremlin.concord.dao.model.GroupedPhraseVote;
import com.rabidgremlin.concord.functions.GetEligiblePhrasesForCompletionFunction;
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

  private final Logger log = LoggerFactory.getLogger(PhrasesResource.class);

  private final PhrasesDao phrasesDao;

  private final VotesDao votesDao;

  private final UploadDao uploadDao;

  private final LabelSuggester labelSuggester;

  private final int consensusLevel;

  private final boolean completeOnTrash;

  private static final String LABEL_TRASH = "TRASH";

  private static final String RESOLVER_USER_ID = "RESOLVER";

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
    log.info("{} getting next phrase to label", caller);

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
      List<PossibleLabel> possibleLabels = suggestedLabels.stream()
          .map(suggestedLabel -> {
            PossibleLabel tempLabel = new PossibleLabel();
            tempLabel.setLabel(suggestedLabel.getLabel());
            tempLabel.setLongDescription(suggestedLabel.getLongDescription());
            tempLabel.setScore(suggestedLabel.getScore());
            tempLabel.setShortDescription(suggestedLabel.getShortDescription());
            return tempLabel;
          })
          .collect(Collectors.toList());

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
  @Consumes(MediaType.APPLICATION_JSON)
  @Timed
  public Response addPhrases(@ApiParam(hidden = true) @Auth Caller caller, UnlabelledPhrases unlabelledPhrases)
  {
    log.info("{} uploading {} phrases as json.", caller, unlabelledPhrases.getUnlabelledPhrases().size());
    log.debug("{}", unlabelledPhrases);

    uploadDao.uploadUnlabelledPhrases(unlabelledPhrases.getUnlabelledPhrases());

    return Response.created(uriInfo.getAbsolutePath()).build();
  }

  @POST
  @Path("bulk")
  @Consumes("text/csv")
  @Timed
  public Response uploadCsv(@ApiParam(hidden = true) @Auth Caller caller, List<UnlabelledPhrase> unlabelledPhrases)
  {
    log.info("{} uploading {} phrases as csv.", caller, unlabelledPhrases.size());
    log.debug("{}", unlabelledPhrases);

    uploadDao.uploadUnlabelledPhrases(unlabelledPhrases);

    return Response.created(uriInfo.getAbsolutePath()).build();
  }

  @GET
  @Path("completed")
  @Produces("text/csv")
  @Timed
  public synchronized Response downloadCsv(@ApiParam(hidden = true) @Auth Caller caller)
  {
    log.info("{} marking phrases and downloading csv of completedPhrases", caller);

    log.debug("Searching for votes over margin {}...", consensusLevel);
    List<GroupedPhraseVote> phraseVotes = votesDao.getTop2LabelsForUncompletedPhrases();
    log.debug("Found {} votes over margin.", phraseVotes.size());

    Map<String, String> phrasesVotedOnByResolver = votesDao.getVotesMadeByUser(RESOLVER_USER_ID).stream()
        .collect(Collectors.toMap(PhraseLabel::getPhraseId, PhraseLabel::getLabel));
    GetEligiblePhrasesForCompletionFunction getPhrases = new GetEligiblePhrasesForCompletionFunction(phraseVotes, phrasesVotedOnByResolver, consensusLevel);

    log.debug("Looking for completed phrases...");
    Set<Phrase> completedPhrases = getPhrases.execute();

    log.debug("Marking phrases complete...");
    phrasesDao.markPhrasesComplete(completedPhrases.stream().map(Phrase::getPhraseId).collect(Collectors.toList()),
        completedPhrases.stream().map(Phrase::getLabel).collect(Collectors.toList()));
    log.debug("Marking phrases complete done.");

    return Response.ok().entity(completedPhrases).build();
  }

  @DELETE
  @Path("completed")
  @Timed
  public Response purgeCompletedPhrasesAndVotes(@ApiParam(hidden = true) @Auth Caller caller)
  {
    log.info("{} purging completed votes and phrases", caller);

    List<String> phraseIdentifiers = phrasesDao.getCompletedPhraseIdentifiers();
    int amount = phraseIdentifiers.size();

    votesDao.deleteAllVotesForPhrase(phraseIdentifiers);
    phrasesDao.deletePhrases(phraseIdentifiers);

    log.info(amount + " phrases purged from database.");

    return Response.ok().build();
  }

  @POST
  @Timed
  @Path("/{phraseId}/votes")
  // TODO: Create proper model class for incoming label
  public Response voteForPhrase(@ApiParam(hidden = true) @Auth Caller caller, @PathParam("phraseId") String phraseId, Label label)
  {
    String labelText = label.getLabel();
    log.info("{} casting vote for phrase[{}] as [{}]", caller, phraseId, labelText);

    castVote(phraseId, labelText, caller.getToken());

    return Response.created(uriInfo.getAbsolutePath()).build();
  }

  private void castVote(String phraseId, String label, String userId)
  {
    votesDao.upsert(phraseId, label, userId);

    // are we marking trashed phrases as completed?
    if (completeOnTrash && StringUtils.equals(LABEL_TRASH, label))
    {
      phrasesDao.markPhraseComplete(phraseId, LABEL_TRASH);
    }
  }

  @POST
  @Timed
  @Path("/{phraseId}/resolve")
  public Response resolvePhrase(@ApiParam(hidden = true) @Auth Caller caller, @PathParam("phraseId") String phraseId, Label label)
  {
    String labelText = label.getLabel();
    log.info("{} resolving phrase[{}] as [{}]", caller, phraseId, labelText);

    castVote(phraseId, labelText, RESOLVER_USER_ID);

    return Response.created(uriInfo.getAbsolutePath()).build();
  }

  @DELETE
  @Path("/{phraseId}/votes/delete")
  @Timed
  public Response purgeVotesForPhrase(@ApiParam(hidden = true) @Auth Caller caller, @PathParam("phraseId") String phraseId)
  {
    log.info("{} purging votes for phrase[{}]", caller, phraseId);

    votesDao.deleteAllVotesForPhrase(Collections.singletonList(phraseId));

    return Response.ok().build();
  }

}
