package com.rabidgremlin.concord.functions;

import java.util.LinkedList;
import java.util.List;

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

  private final int consensusLevel;

  private final Logger log = LoggerFactory.getLogger(GetEligiblePhrasesForCompletionFunction.class);

  public GetEligiblePhrasesForCompletionFunction(List<GroupedPhraseVote> phraseVotes, int consensusLevel)
  {
    this.phraseVotes = phraseVotes;
    this.consensusLevel = consensusLevel;
  }

  public List<Phrase> execute()
  {
    List<Phrase> completedPhrases = new LinkedList<>();

    for (int i = 0; i < phraseVotes.size(); i++)
    {
      GroupedPhraseVote highestVote = phraseVotes.get(i);
      GroupedPhraseVote secondHighestVote = null;
      // in case it is the last one in the list
      if (i != phraseVotes.size() - 1)
      {
        secondHighestVote = phraseVotes.get(i + 1);
      }
      // When there is no second vote, we just need to check if it has enough votes
      log.debug("Highest: Label {} voted {} times for phrase[{}]", highestVote.getLabel(), highestVote.getVoteCount(), highestVote.getText());
      if (secondHighestVote == null || !highestVote.getPhraseId().equals(secondHighestVote.getPhraseId()))
      {
        log.debug("There is no second vote for phrase[{}]", highestVote.getText());
        if (highestVote.getVoteCount() >= consensusLevel)
        {
          addVoteToCompletedList(completedPhrases, highestVote);
        }
      }
      // When there is a second vote, calculate the vote count difference
      else
      {
        i++;
        log.debug("Second highest: Label {} voted {} times for phrase[{}]", secondHighestVote.getLabel(),
            secondHighestVote.getVoteCount(),
            secondHighestVote.getText());
        if (voteDifferenceIsEligibleForCompletion(highestVote.getVoteCount(), secondHighestVote.getVoteCount()))
        {
          addVoteToCompletedList(completedPhrases, highestVote);
        }
      }
    }

    return completedPhrases;
  }

  private boolean voteDifferenceIsEligibleForCompletion(int highestLabelVoteCount, int secondHighestLabelVoteCount)
  {
    return highestLabelVoteCount - secondHighestLabelVoteCount >= consensusLevel;
  }

  private void addVoteToCompletedList(List<Phrase> list, GroupedPhraseVote vote)
  {
    Phrase curr = new Phrase();
    curr.setLabel(vote.getLabel());
    curr.setText(vote.getText());
    curr.setPhraseId(vote.getPhraseId());
    list.add(curr);
    log.debug("Found completed phrase [{}] with id {}", vote.getText(), vote.getPhraseId());
  }
}
