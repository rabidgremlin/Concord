package com.rabidgremlin.concord.functions;

import static com.rabidgremlin.concord.api.Phrase.computePhraseId;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.rabidgremlin.concord.api.Phrase;
import com.rabidgremlin.concord.dao.model.GroupedPhraseVote;

public class GetEligiblePhrasesForCompletionFunctionTest
{

  private static final Map<String, String> EMPTY_MAP = ImmutableMap.of();

  @Test
  public void shouldGetEligiblePhrases()
  {
    // Given
    List<GroupedPhraseVote> phraseVotes = new ArrayList<>();

    phraseVotes.add(new GroupedPhraseVote("BonJovi", "Woah, livin' on a prayer", 3));
    phraseVotes.add(new GroupedPhraseVote("Beyonce", "Woah, livin' on a prayer", 1));

    phraseVotes.add(new GroupedPhraseVote("GnarlsBarkley", "I remember, I remember when I lost my mind ", 2));

    phraseVotes.add(new GroupedPhraseVote("EltonJohn", "Pretty eyed, pirate smile, you'll marry a music man", 7));
    phraseVotes.add(new GroupedPhraseVote("DollyParton", "Pretty eyed, pirate smile, you'll marry a music man", 4));

    phraseVotes.add(new GroupedPhraseVote("CelineDion", "And my heart will go on and on", 1));

    phraseVotes.add(new GroupedPhraseVote("TaylorSwift", "Last Christmas I gave your my heart", 3));
    phraseVotes.add(new GroupedPhraseVote("JustinBieber", "Last Christmas I gave your my heart", 2));

    // When
    GetEligiblePhrasesForCompletionFunction functionUnderTest = new GetEligiblePhrasesForCompletionFunction(phraseVotes, EMPTY_MAP, 2);
    List<Phrase> phrasesEligibleForCompletion = functionUnderTest.execute();

    // Then
    assertEquals(3, phrasesEligibleForCompletion.size());

    assertEquals(computePhraseId("Woah, livin' on a prayer"), phrasesEligibleForCompletion.get(0).getPhraseId());
    assertEquals("BonJovi", phrasesEligibleForCompletion.get(0).getLabel());

    assertEquals(computePhraseId("I remember, I remember when I lost my mind "), phrasesEligibleForCompletion.get(1).getPhraseId());
    assertEquals("GnarlsBarkley", phrasesEligibleForCompletion.get(1).getLabel());

    assertEquals(computePhraseId("Pretty eyed, pirate smile, you'll marry a music man"), phrasesEligibleForCompletion.get(2).getPhraseId());
    assertEquals("EltonJohn", phrasesEligibleForCompletion.get(2).getLabel());
  }

  @Test
  public void shouldBypassResolvedPhrases()
  {
    // Given
    Map<String, String> resolvedPhrases = ImmutableMap.of(
        computePhraseId("Woah, livin' on a prayer"), "Bonjovi",
        computePhraseId("Pretty eyed, pirate smile, you'll marry a music man"), "EltonJohn");

    List<GroupedPhraseVote> phraseVotes = new ArrayList<>();

    phraseVotes.add(new GroupedPhraseVote("Beyonce", "Woah, livin' on a prayer", 3));
    phraseVotes.add(new GroupedPhraseVote("Bonjovi", "Woah, livin' on a prayer", 1));

    phraseVotes.add(new GroupedPhraseVote("GnarlsBarkley", "I remember, I remember when I lost my mind ", 2));

    phraseVotes.add(new GroupedPhraseVote("JustinBieber", "Pretty eyed, pirate smile, you'll marry a music man", 7));
    phraseVotes.add(new GroupedPhraseVote("DollyParton", "Pretty eyed, pirate smile, you'll marry a music man", 4));

    phraseVotes.add(new GroupedPhraseVote("CelineDion", "And my heart will go on and on", 1));

    phraseVotes.add(new GroupedPhraseVote("TaylorSwift", "Last Christmas I gave your my heart", 3));
    phraseVotes.add(new GroupedPhraseVote("JustinBieber", "Last Christmas I gave your my heart", 2));

    // When
    GetEligiblePhrasesForCompletionFunction functionUnderTest = new GetEligiblePhrasesForCompletionFunction(phraseVotes, resolvedPhrases, 2);
    List<Phrase> phrasesEligibleForCompletion = functionUnderTest.execute();

    // Then
    assertEquals(3, phrasesEligibleForCompletion.size());

    assertEquals(computePhraseId("Woah, livin' on a prayer"), phrasesEligibleForCompletion.get(0).getPhraseId());
    assertEquals("Bonjovi", phrasesEligibleForCompletion.get(0).getLabel());

    assertEquals(computePhraseId("I remember, I remember when I lost my mind "), phrasesEligibleForCompletion.get(1).getPhraseId());
    assertEquals("GnarlsBarkley", phrasesEligibleForCompletion.get(1).getLabel());

    assertEquals(computePhraseId("Pretty eyed, pirate smile, you'll marry a music man"), phrasesEligibleForCompletion.get(2).getPhraseId());
    assertEquals("EltonJohn", phrasesEligibleForCompletion.get(2).getLabel());
  }

}
