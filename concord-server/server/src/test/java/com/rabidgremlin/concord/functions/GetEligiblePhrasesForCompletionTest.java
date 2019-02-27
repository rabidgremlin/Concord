package com.rabidgremlin.concord.functions;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.rabidgremlin.concord.api.Phrase;
import com.rabidgremlin.concord.dao.GroupedPhraseVote;

public class GetEligiblePhrasesForCompletionTest
{
  private GetEligiblePhrasesForCompletion functionUnderTest;

  private GroupedPhraseVote vote2, vote5;

  @Mock
  UriInfo uriInfoMock;

  @Before
  public void setUp()
  {
    uriInfoMock = mock(UriInfo.class);

    List<GroupedPhraseVote> phraseVotes = new ArrayList<>();

    phraseVotes.add(new GroupedPhraseVote("123", "BonJovi", "Woah, livin' on a prayer", 3));

    vote2 = new GroupedPhraseVote("123", "Beyonce", "Woah, livin' on a prayer", 1);
    phraseVotes.add(vote2);

    phraseVotes.add(new GroupedPhraseVote("124", "GnarlsBarkley", "I remember, I remember when I lost my mind ", 2));

    phraseVotes.add(new GroupedPhraseVote("125", "EltonJohn", "Pretty eyed, pirate smile, you'll marry a music man", 7));

    vote5 = new GroupedPhraseVote("125", "DollyParton", "Pretty eyed, pirate smile, you'll marry a music man", 4);
    phraseVotes.add(vote5);

    phraseVotes.add(new GroupedPhraseVote("126", "CelineDion", "And my heart will go on and on", 1));

    phraseVotes.add(new GroupedPhraseVote("127", "TaylorSwift", "Last Christmas I gave your my heart", 3));

    phraseVotes.add(new GroupedPhraseVote("127", "JustinBieber", "Last Christmas I gave your my heart", 2));

    functionUnderTest = new GetEligiblePhrasesForCompletion(phraseVotes, 2);
  }

  @Test
  public void canGetEligiblePhrases()
  {
    List<Phrase> result = functionUnderTest.execute();

    assertEquals(3, result.size());

    assertEquals("123", result.get(0).getPhraseId());
    assertEquals("BonJovi", result.get(0).getLabel());

    assertEquals("124", result.get(1).getPhraseId());
    assertEquals("GnarlsBarkley", result.get(1).getLabel());

    assertEquals("125", result.get(2).getPhraseId());
    assertEquals("EltonJohn", result.get(2).getLabel());
  }
}
