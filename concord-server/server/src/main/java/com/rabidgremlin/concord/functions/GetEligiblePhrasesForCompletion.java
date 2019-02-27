package com.rabidgremlin.concord.functions;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabidgremlin.concord.api.Phrase;
import com.rabidgremlin.concord.dao.GroupedPhraseVote;

/**
 * function retrieves phrases that have label votes with a label vote count greater than the consensus level, and a
 * label vote count that is greater than the second highest label vote count for that phrase, by at least the consensus
 * level.
 */

public class GetEligiblePhrasesForCompletion
{

  private List<GroupedPhraseVote> phraseVotes;

  private int consensusLevel;

  private Logger log = LoggerFactory.getLogger(GetEligiblePhrasesForCompletion.class);

  public GetEligiblePhrasesForCompletion(List<GroupedPhraseVote> phraseVotes, int consensusLevel)
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
        log.debug("Second highest: Label {} voted {} times for phrase[{}] with second hightest vote count {}", secondHighestVote.getLabel(),
            secondHighestVote.getVoteCount(),
            secondHighestVote.getText());
        if (highestVote.getVoteCount() - secondHighestVote.getVoteCount() >= consensusLevel)
        {
          addVoteToCompletedList(completedPhrases, highestVote);
        }
      }
    }

    return completedPhrases;
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
