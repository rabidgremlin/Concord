package com.rabidgremlin.concord.functions;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.rabidgremlin.concord.api.Phrase;
import com.rabidgremlin.concord.dao.model.GroupedPhraseVote;

public class GetEligiblePhrasesForCompletionFunctionTest
{

  private GetEligiblePhrasesForCompletionFunction functionUnderTest;

  @Before
  public void setUp()
  {
    MockitoAnnotations.initMocks(this);

    List<GroupedPhraseVote> phraseVotes = new ArrayList<>();

    phraseVotes.add(new GroupedPhraseVote("123", "BonJovi", "Woah, livin' on a prayer", 3));
    phraseVotes.add(new GroupedPhraseVote("123", "Beyonce", "Woah, livin' on a prayer", 1));

    phraseVotes.add(new GroupedPhraseVote("124", "GnarlsBarkley", "I remember, I remember when I lost my mind ", 2));

    phraseVotes.add(new GroupedPhraseVote("125", "EltonJohn", "Pretty eyed, pirate smile, you'll marry a music man", 7));
    phraseVotes.add(new GroupedPhraseVote("125", "DollyParton", "Pretty eyed, pirate smile, you'll marry a music man", 4));

    phraseVotes.add(new GroupedPhraseVote("126", "CelineDion", "And my heart will go on and on", 1));

    phraseVotes.add(new GroupedPhraseVote("127", "TaylorSwift", "Last Christmas I gave your my heart", 3));
    phraseVotes.add(new GroupedPhraseVote("127", "JustinBieber", "Last Christmas I gave your my heart", 2));

    functionUnderTest = new GetEligiblePhrasesForCompletionFunction(phraseVotes, 2);
  }

  @Test
  public void canGetEligiblePhrases()
  {
    List<Phrase> phrasesEligibleForCompletion = functionUnderTest.execute();

    assertEquals(3, phrasesEligibleForCompletion.size());

    assertEquals("123", phrasesEligibleForCompletion.get(0).getPhraseId());
    assertEquals("BonJovi", phrasesEligibleForCompletion.get(0).getLabel());

    assertEquals("124", phrasesEligibleForCompletion.get(1).getPhraseId());
    assertEquals("GnarlsBarkley", phrasesEligibleForCompletion.get(1).getLabel());

    assertEquals("125", phrasesEligibleForCompletion.get(2).getPhraseId());
    assertEquals("EltonJohn", phrasesEligibleForCompletion.get(2).getLabel());
  }

}
