package com.rabidgremlin.concord.functions;

import static com.rabidgremlin.concord.api.Phrase.computePhraseId;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
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
    Set<Phrase> phrasesEligibleForCompletion = functionUnderTest.execute();

    // Then
    Set<Phrase> expected = ImmutableSet.of(
        Phrase.incomplete("Woah, livin' on a prayer", "BonJovi"),
        Phrase.incomplete("I remember, I remember when I lost my mind ", "GnarlsBarkley"),
        Phrase.incomplete("Pretty eyed, pirate smile, you'll marry a music man", "EltonJohn"));
    assertThat(phrasesEligibleForCompletion.size(), is(expected.size()));
    assertThat(phrasesEligibleForCompletion, containsInAnyOrder(expected.toArray()));
  }

  @Test
  public void shouldBypassResolvedPhrasesForPhrasesWithVotes()
  {
    // Given
    Map<String, String> resolvedPhrases = ImmutableMap.of(
        computePhraseId("Woah, livin' on a prayer"), "RESOLVED_1",
        computePhraseId("Pretty eyed, pirate smile, you'll marry a music man"), "RESOLVED_2",
        computePhraseId("Never Gonna Give You Up"), "RESOLVED_NOT_VOTED_FOR");

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
    Set<Phrase> phrasesEligibleForCompletion = functionUnderTest.execute();

    // Then
    Set<Phrase> expected = ImmutableSet.of(
        Phrase.incomplete("Woah, livin' on a prayer", "RESOLVED_1"),
        Phrase.incomplete("I remember, I remember when I lost my mind ", "GnarlsBarkley"),
        Phrase.incomplete("Pretty eyed, pirate smile, you'll marry a music man", "RESOLVED_2"));
    assertThat(phrasesEligibleForCompletion.size(), is(expected.size()));
    assertThat(phrasesEligibleForCompletion, containsInAnyOrder(expected.toArray()));
  }

}
