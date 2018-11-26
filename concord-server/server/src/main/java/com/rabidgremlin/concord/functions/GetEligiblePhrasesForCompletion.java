package com.rabidgremlin.concord.functions;

import com.rabidgremlin.concord.api.Phrase;
import com.rabidgremlin.concord.dao.GroupedPhraseVote;
import com.rabidgremlin.concord.dao.VotesDao;

import java.util.LinkedList;
import java.util.List;

/**
 * function retrieves phrases that have label votes with a label vote count greater than the consensus level,
 * and a label vote count that is greater than the second highest label vote count for that phrase, by at least the consensus level.
 */

public class GetEligiblePhrasesForCompletion {

    private VotesDao votesDao;
    private List<GroupedPhraseVote> phraseVotes;
    private int consensusLevel;

    public GetEligiblePhrasesForCompletion(VotesDao votesDao, List<GroupedPhraseVote> phraseVotes, int consensusLevel)
    {
        this.votesDao = votesDao;
        this.phraseVotes = phraseVotes;
        this.consensusLevel = consensusLevel;
    }

    public List<Phrase> execute()
    {
        List<Phrase> completedPhrases = new LinkedList<>();

        for(GroupedPhraseVote vote : phraseVotes)
        {
            String phraseId = vote.getPhraseId();
            String text = vote.getText();
            String designatedLabel = vote.getLabel();

            int secondHighestContenderCount = 0;
            GroupedPhraseVote secondHighestContender = votesDao.getSecondHighestContender(phraseId);

            if(secondHighestContender != null)
            {
                secondHighestContenderCount = secondHighestContender.getVoteCount();
            }

            if(vote.getVoteCount() - secondHighestContenderCount >= consensusLevel)
            {
                Phrase curr = new Phrase();
                curr.setLabel(designatedLabel);
                curr.setText(text);
                curr.setPhraseId(phraseId);
                completedPhrases.add(curr);
            }
        }
        return completedPhrases;
    }
}
