package com.rabidgremlin.concord.functions;

import static java.time.LocalDateTime.now;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.rabidgremlin.concord.api.DeadLockedPhrase;
import com.rabidgremlin.concord.api.Phrase;
import com.rabidgremlin.concord.dao.model.GroupedPhraseVoteWithMostRecentVoteTime;

public class GetDeadLockedPhrasesFunctionTest
{

  private static final Timestamp TIME_STAMP = Timestamp.valueOf(LocalDateTime.of(1, 1, 1, 1, 1));

  private static final Set<String> EMPTY_SET = ImmutableSet.of();

  @Test
  public void shouldGetDeadLockedPhraseWith2LabelsVotedOn()
  {
    // Given
    List<GroupedPhraseVoteWithMostRecentVoteTime> phraseVotes = new ArrayList<>();

    phraseVotes.add(new GroupedPhraseVoteWithMostRecentVoteTime("BonJovi", "Woah, livin' on a prayer", 4, TIME_STAMP));
    phraseVotes.add(new GroupedPhraseVoteWithMostRecentVoteTime("Beyonce", "Woah, livin' on a prayer", 3, TIME_STAMP));

    // When
    GetDeadLockedPhrasesFunction function = new GetDeadLockedPhrasesFunction(phraseVotes, EMPTY_SET, 3);
    List<DeadLockedPhrase> deadLockedPhrases = function.execute(7);

    // Then
    assertThat(deadLockedPhrases.size(), is(1));

    assertThat(deadLockedPhrases.get(0).getPhrase().getText(), is("Woah, livin' on a prayer"));
    assertThat(deadLockedPhrases.get(0).getLabelsInVoteOrder().size(), is(2));
  }

  @Test
  public void shouldIgnorePhrasesWhichHaveSufficientRemainingVotes()
  {
    // Given
    List<GroupedPhraseVoteWithMostRecentVoteTime> phraseVotes = new ArrayList<>();

    phraseVotes.add(new GroupedPhraseVoteWithMostRecentVoteTime("BonJovi", "Woah, livin' on a prayer", 3, TIME_STAMP));
    phraseVotes.add(new GroupedPhraseVoteWithMostRecentVoteTime("Beyonce", "Woah, livin' on a prayer", 2, TIME_STAMP));

    // When
    GetDeadLockedPhrasesFunction function = new GetDeadLockedPhrasesFunction(phraseVotes, EMPTY_SET, 3);
    List<DeadLockedPhrase> deadLockedPhrases = function.execute(7);

    // Then
    assertThat(deadLockedPhrases.size(), is(0));
  }

  @Test
  public void shouldConsiderAllLabelsVotedOn()
  {
    // Given
    List<GroupedPhraseVoteWithMostRecentVoteTime> phraseVotes = new ArrayList<>();

    phraseVotes.add(new GroupedPhraseVoteWithMostRecentVoteTime("BonJovi", "Woah, livin' on a prayer", 3, TIME_STAMP));
    phraseVotes.add(new GroupedPhraseVoteWithMostRecentVoteTime("Beyonce", "Woah, livin' on a prayer", 2, TIME_STAMP));
    phraseVotes.add(new GroupedPhraseVoteWithMostRecentVoteTime("JustinBieber", "Woah, livin' on a prayer", 1, TIME_STAMP));
    phraseVotes.add(new GroupedPhraseVoteWithMostRecentVoteTime("TaylorSwift", "Woah, livin' on a prayer", 1, TIME_STAMP));

    // When
    GetDeadLockedPhrasesFunction function = new GetDeadLockedPhrasesFunction(phraseVotes, EMPTY_SET, 3);
    List<DeadLockedPhrase> deadLockedPhrases = function.execute(7);

    // Then
    assertThat(deadLockedPhrases.size(), is(1));

    assertThat(deadLockedPhrases.get(0).getPhrase().getText(), is("Woah, livin' on a prayer"));
    assertThat(deadLockedPhrases.get(0).getLabelsInVoteOrder().size(), is(4));
  }

  @Test
  public void shouldIncludePhrasesWhoseHighestLabelIsLessThanConsensus()
  {
    // Given
    List<GroupedPhraseVoteWithMostRecentVoteTime> phraseVotes = new ArrayList<>();

    phraseVotes.add(new GroupedPhraseVoteWithMostRecentVoteTime("BonJovi", "Woah, livin' on a prayer", 2, TIME_STAMP));
    phraseVotes.add(new GroupedPhraseVoteWithMostRecentVoteTime("Beyonce", "Woah, livin' on a prayer", 2, TIME_STAMP));
    phraseVotes.add(new GroupedPhraseVoteWithMostRecentVoteTime("JustinBieber", "Woah, livin' on a prayer", 2, TIME_STAMP));
    phraseVotes.add(new GroupedPhraseVoteWithMostRecentVoteTime("TaylorSwift", "Woah, livin' on a prayer", 1, TIME_STAMP));

    // When
    GetDeadLockedPhrasesFunction function = new GetDeadLockedPhrasesFunction(phraseVotes, EMPTY_SET, 3);
    List<DeadLockedPhrase> deadLockedPhrases = function.execute(7);

    // Then
    assertThat(deadLockedPhrases.size(), is(1));

    assertThat(deadLockedPhrases.get(0).getPhrase().getText(), is("Woah, livin' on a prayer"));
    assertThat(deadLockedPhrases.get(0).getLabelsInVoteOrder().size(), is(4));
  }

  @Test
  public void shouldSortByPhrasesWhichWereMostRecentlyVotedOn()
  {
    // Given
    List<GroupedPhraseVoteWithMostRecentVoteTime> phraseVotes = new ArrayList<>();
    Timestamp now = Timestamp.valueOf(now());
    Timestamp oneDayAgo = Timestamp.valueOf(now().minusDays(1));
    Timestamp twoDaysAgo = Timestamp.valueOf(now().minusDays(2));
    Timestamp oneWeekAgo = Timestamp.valueOf(now().minusWeeks(1));

    phraseVotes.add(new GroupedPhraseVoteWithMostRecentVoteTime("TaylorSwift", "Last Christmas I gave your my heart", 4, now));
    phraseVotes.add(new GroupedPhraseVoteWithMostRecentVoteTime("JustinBieber", "Last Christmas I gave your my heart", 3, now));

    phraseVotes.add(new GroupedPhraseVoteWithMostRecentVoteTime("TaylorSwift", "Half way there", 4, oneWeekAgo));
    phraseVotes.add(new GroupedPhraseVoteWithMostRecentVoteTime("JustinBieber", "Half way there", 3, oneWeekAgo));

    phraseVotes.add(new GroupedPhraseVoteWithMostRecentVoteTime("TaylorSwift", "Woah, livin' on a prayer", 4, oneDayAgo));
    phraseVotes.add(new GroupedPhraseVoteWithMostRecentVoteTime("JustinBieber", "Woah, livin' on a prayer", 3, oneDayAgo));

    phraseVotes.add(new GroupedPhraseVoteWithMostRecentVoteTime("TaylorSwift", "I remember, I remember when I lost my mind", 4, twoDaysAgo));

    // When
    GetDeadLockedPhrasesFunction function = new GetDeadLockedPhrasesFunction(phraseVotes, EMPTY_SET, 3);
    List<DeadLockedPhrase> deadLockedPhrases = function.execute(7);

    // Then
    assertThat(deadLockedPhrases.size(), is(3));

    assertThat(deadLockedPhrases.get(0).getPhrase().getText(), is("Last Christmas I gave your my heart"));
    assertThat(deadLockedPhrases.get(0).getLabelsInVoteOrder().size(), is(2));

    assertThat(deadLockedPhrases.get(1).getPhrase().getText(), is("Woah, livin' on a prayer"));
    assertThat(deadLockedPhrases.get(1).getLabelsInVoteOrder().size(), is(2));

    assertThat(deadLockedPhrases.get(2).getPhrase().getText(), is("Half way there"));
    assertThat(deadLockedPhrases.get(2).getLabelsInVoteOrder().size(), is(2));
  }

  @Test
  public void shouldSortPhraseLabelsByHighestVoteCount()
  {
    // Given
    List<GroupedPhraseVoteWithMostRecentVoteTime> phraseVotes = new ArrayList<>();

    phraseVotes.add(new GroupedPhraseVoteWithMostRecentVoteTime("BonJovi", "Woah, livin' on a prayer", 2, TIME_STAMP));
    phraseVotes.add(new GroupedPhraseVoteWithMostRecentVoteTime("Bob", "Woah, livin' on a prayer", 1, TIME_STAMP));
    phraseVotes.add(new GroupedPhraseVoteWithMostRecentVoteTime("Alice", "Woah, livin' on a prayer", 1, TIME_STAMP));
    phraseVotes.add(new GroupedPhraseVoteWithMostRecentVoteTime("Eve", "Woah, livin' on a prayer", 3, TIME_STAMP));
    phraseVotes.add(new GroupedPhraseVoteWithMostRecentVoteTime("Beyonce", "Woah, livin' on a prayer", 5, TIME_STAMP));

    // When
    GetDeadLockedPhrasesFunction function = new GetDeadLockedPhrasesFunction(phraseVotes, EMPTY_SET, 3);
    List<DeadLockedPhrase> deadLockedPhrases = function.execute(12);

    // Then
    assertThat(deadLockedPhrases.size(), is(1));

    assertThat(deadLockedPhrases.get(0).getPhrase().getText(), is("Woah, livin' on a prayer"));
    assertThat(deadLockedPhrases.get(0).getLabelsInVoteOrder().size(), is(5));
    assertThat(deadLockedPhrases.get(0).getLabelsInVoteOrder().get(0).getLabel(), is("Beyonce"));
    assertThat(deadLockedPhrases.get(0).getLabelsInVoteOrder().get(0).getCount(), is(5));
    assertThat(deadLockedPhrases.get(0).getLabelsInVoteOrder().get(1).getLabel(), is("Eve"));
    assertThat(deadLockedPhrases.get(0).getLabelsInVoteOrder().get(1).getCount(), is(3));
    assertThat(deadLockedPhrases.get(0).getLabelsInVoteOrder().get(2).getLabel(), is("BonJovi"));
    assertThat(deadLockedPhrases.get(0).getLabelsInVoteOrder().get(2).getCount(), is(2));
    assertThat(deadLockedPhrases.get(0).getLabelsInVoteOrder().get(3).getLabel(), is("Bob"));
    assertThat(deadLockedPhrases.get(0).getLabelsInVoteOrder().get(3).getCount(), is(1));
    assertThat(deadLockedPhrases.get(0).getLabelsInVoteOrder().get(4).getLabel(), is("Alice"));
    assertThat(deadLockedPhrases.get(0).getLabelsInVoteOrder().get(4).getCount(), is(1));
  }

  @Test
  public void shouldIgnoreResolvedPhrases()
  {
    // Given
    Set<String> resolvedPhrases = ImmutableSet.of(Phrase.computePhraseId("Woah, livin' on a prayer"));

    List<GroupedPhraseVoteWithMostRecentVoteTime> phraseVotes = new ArrayList<>();

    phraseVotes.add(new GroupedPhraseVoteWithMostRecentVoteTime("BonJovi", "Woah, livin' on a prayer", 4, TIME_STAMP));
    phraseVotes.add(new GroupedPhraseVoteWithMostRecentVoteTime("Beyonce", "Woah, livin' on a prayer", 3, TIME_STAMP));

    // When
    GetDeadLockedPhrasesFunction function = new GetDeadLockedPhrasesFunction(phraseVotes, resolvedPhrases, 3);
    List<DeadLockedPhrase> deadLockedPhrases = function.execute(7);

    // Then
    assertThat(deadLockedPhrases.size(), is(0));
  }

  @Test
  public void shouldIgnorePhrasesWhichHaveOnlyOneLabelVotedOn()
  {
    // Given
    List<GroupedPhraseVoteWithMostRecentVoteTime> phraseVotes = new ArrayList<>();

    phraseVotes.add(new GroupedPhraseVoteWithMostRecentVoteTime("BonJovi", "Woah, livin' on a prayer", 7, TIME_STAMP));

    // When
    GetDeadLockedPhrasesFunction function = new GetDeadLockedPhrasesFunction(phraseVotes, EMPTY_SET, 3);
    List<DeadLockedPhrase> deadLockedPhrases = function.execute(7);

    // Then
    assertThat(deadLockedPhrases.size(), is(0));
  }

  @Test
  public void shouldAccountForFilteredUsersVotes()
  {
    // Given
    // 7 users but 8 votes (BULK_UPLOAD has voted)
    final int userCount = 7;
    final int consensusLevel = 3;
    List<GroupedPhraseVoteWithMostRecentVoteTime> phraseVotes = new ArrayList<>();

    phraseVotes.add(new GroupedPhraseVoteWithMostRecentVoteTime("BonJovi", "Woah, livin' on a prayer", 5, TIME_STAMP));
    phraseVotes.add(new GroupedPhraseVoteWithMostRecentVoteTime("Beyonce", "Woah, livin' on a prayer", 2, TIME_STAMP));
    phraseVotes.add(new GroupedPhraseVoteWithMostRecentVoteTime("Elvis", "Woah, livin' on a prayer", 1, TIME_STAMP));

    // When
    GetDeadLockedPhrasesFunction function = new GetDeadLockedPhrasesFunction(phraseVotes, EMPTY_SET, consensusLevel);
    List<DeadLockedPhrase> deadLockedPhrases = function.execute(userCount);

    // Then
    assertThat(deadLockedPhrases.size(), is(0));
  }

}
