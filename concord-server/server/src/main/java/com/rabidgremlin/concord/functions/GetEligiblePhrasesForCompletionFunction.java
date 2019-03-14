package com.rabidgremlin.concord.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

  private final List<GroupedPhraseVote> phraseVotes;

  private final Map<String, String> phrasesVotedOnByResolver;

  private final int consensusLevel;

  private final Logger log = LoggerFactory.getLogger(GetEligiblePhrasesForCompletionFunction.class);

  public GetEligiblePhrasesForCompletionFunction(List<GroupedPhraseVote> phraseVotes, Map<String, String> phrasesVotedOnByResolver, int consensusLevel)
  {
    this.phraseVotes = phraseVotes;
    this.phrasesVotedOnByResolver = phrasesVotedOnByResolver;
    this.consensusLevel = consensusLevel;
  }

  public List<Phrase> execute()
  {
    List<Phrase> completedPhrases = new ArrayList<>();

    for (int i = 0; i < phraseVotes.size(); i++)
    {
      GroupedPhraseVote highestVote = phraseVotes.get(i);
      GroupedPhraseVote secondHighestVote = null;
      if (i != phraseVotes.size() - 1)
      {
        secondHighestVote = phraseVotes.get(i + 1);
      }
      boolean isNoSecondVote = secondHighestVote == null || !highestVote.getPhraseId().equals(secondHighestVote.getPhraseId());

      // Check if it was voted on by the resolver
      if (phrasesVotedOnByResolver.containsKey(highestVote.getPhraseId()))
      {
        String resolvedLabel = phrasesVotedOnByResolver.get(highestVote.getPhraseId());
        log.debug("Deadlocked phrase[{}] was marked as resolved with label {}.", highestVote.getText(), resolvedLabel);
        addVoteToCompletedList(completedPhrases, resolvedLabel, highestVote.getText(), highestVote.getPhraseId());
        if (!isNoSecondVote)
        {
          i++;
        }
        continue;
      }

      log.debug("Highest: Label {} voted {} times for phrase[{}]", highestVote.getLabel(), highestVote.getVoteCount(), highestVote.getText());
      // When there is no second vote, we just need to check if it has enough votes
      if (isNoSecondVote)
      {
        log.debug("There is no second vote for phrase[{}]", highestVote.getText());
        if (highestVote.getVoteCount() >= consensusLevel)
        {
          addVoteToCompletedList(completedPhrases, highestVote.getLabel(), highestVote.getText(), highestVote.getPhraseId());
        }
      }
      // When there is a second vote, calculate the vote count difference
      else
      {
        i++;
        log.debug("Second highest: Label {} voted {} times for phrase[{}]", secondHighestVote.getLabel(),
            secondHighestVote.getVoteCount(), secondHighestVote.getText());
        if (highestVote.getVoteCount() - secondHighestVote.getVoteCount() >= consensusLevel)
        {
          addVoteToCompletedList(completedPhrases, highestVote.getLabel(), highestVote.getText(), highestVote.getPhraseId());
        }
      }
    }
    log.info("Found {} completed phrase(s).", completedPhrases.size());
    return completedPhrases;
  }

  private void addVoteToCompletedList(List<Phrase> list, String label, String text, String phraseId)
  {
    Phrase curr = new Phrase();
    curr.setLabel(label);
    curr.setText(text);
    curr.setPhraseId(phraseId);
    list.add(curr);
    log.debug("Found completed phrase [{}] with id {}", text, phraseId);
  }
}
