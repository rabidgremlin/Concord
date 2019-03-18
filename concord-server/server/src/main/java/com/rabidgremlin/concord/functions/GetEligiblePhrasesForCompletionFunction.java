package com.rabidgremlin.concord.functions;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabidgremlin.concord.api.Phrase;
import com.rabidgremlin.concord.dao.model.GroupedPhraseVote;

/**
 * Function determines phrases which are eligible for completion. I.e. they have a label with vote count greater than
 * the consensus AND a difference between the top two label vote counts less than consensus.
 */
public final class GetEligiblePhrasesForCompletionFunction
{

  private final List<GroupedPhraseVote> phrasesVotes;

  private final Map<String, String> phrasesVotedOnByResolver;

  private final int consensusLevel;

  private final Logger log = LoggerFactory.getLogger(GetEligiblePhrasesForCompletionFunction.class);

  public GetEligiblePhrasesForCompletionFunction(List<GroupedPhraseVote> phrasesVotes, Map<String, String> phrasesVotedOnByResolver, int consensusLevel)
  {
    this.phrasesVotes = phrasesVotes;
    this.phrasesVotedOnByResolver = phrasesVotedOnByResolver;
    this.consensusLevel = consensusLevel;
  }

  public Set<Phrase> execute()
  {
    Set<Phrase> completedPhrases = phrasesVotes.stream()
        .collect(Collectors.groupingBy(GroupedPhraseVote::getPhraseId))
        .values().stream()
        .map(this::sortByHighestVotedLabel)
        .filter(this::isEligibleForCompletion)
        .map(this::extractHighestVotedLabel)
        .collect(Collectors.toSet());

    log.info("Found {} completed phrase(s).", completedPhrases.size());
    return completedPhrases;
  }

  private List<GroupedPhraseVote> sortByHighestVotedLabel(List<GroupedPhraseVote> phraseVotes)
  {
    return phraseVotes.stream().sorted(Comparator.comparingInt(GroupedPhraseVote::getVoteCount).reversed()).collect(Collectors.toList());
  }

  private boolean isEligibleForCompletion(List<GroupedPhraseVote> phraseVotes)
  {
    GroupedPhraseVote highestVote = phraseVotes.get(0);

    if (phrasesVotedOnByResolver.containsKey(highestVote.getPhraseId()))
    {
      // Resolver votes bypasses consensus check
      String resolvedLabel = phrasesVotedOnByResolver.get(highestVote.getPhraseId());
      highestVote.setLabel(resolvedLabel);
      log.debug("Deadlocked phrase[{}] was marked as resolved with label {}.", highestVote.getText(), highestVote.getLabel());
      return true;
    }

    log.debug("Highest: Label {} voted {} times for phrase[{}]", highestVote.getLabel(), highestVote.getVoteCount(), highestVote.getText());
    if (phraseVotes.size() == 1)
    {
      // When there is no second vote, we just need to check if it has enough votes
      log.debug("There is no second vote for phrase[{}]", highestVote.getText());
      return highestVote.getVoteCount() >= consensusLevel;
    }
    else
    {
      // When there is a second vote, calculate the vote count difference
      GroupedPhraseVote secondHighestVote = phraseVotes.get(1);
      log.debug("Second highest: Label {} voted {} times for phrase[{}]", secondHighestVote.getLabel(), secondHighestVote.getVoteCount(),
          secondHighestVote.getText());
      return highestVote.getVoteCount() - secondHighestVote.getVoteCount() >= consensusLevel;
    }
  }

  private Phrase extractHighestVotedLabel(List<GroupedPhraseVote> phraseVotes)
  {
    GroupedPhraseVote highestVote = phraseVotes.get(0);
    log.debug("Found completed phrase [{}] with id {}", highestVote.getText(), highestVote.getPhraseId());
    return Phrase.incomplete(highestVote.getPhraseId(), highestVote.getText(), highestVote.getLabel());
  }

}
