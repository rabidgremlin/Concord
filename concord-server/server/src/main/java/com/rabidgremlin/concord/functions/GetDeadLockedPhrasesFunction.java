package com.rabidgremlin.concord.functions;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabidgremlin.concord.api.DeadLockedPhrase;
import com.rabidgremlin.concord.api.LabelCount;
import com.rabidgremlin.concord.api.Phrase;
import com.rabidgremlin.concord.dao.model.GroupedPhraseVote;
import com.rabidgremlin.concord.dao.model.GroupedPhraseVoteWithMostRecentVoteTime;

/**
 * Function determines phrases which are deadlocked. i.e. they can never be considered complete based on the remaining
 * votes and condition in {@link GetEligiblePhrasesForCompletionFunction}
 */
public final class GetDeadLockedPhrasesFunction
{

  private final List<GroupedPhraseVoteWithMostRecentVoteTime> phraseVotes;

  private final Set<String> phrasesVotedOnByResolver;

  private final int consensusLevel;

  private final Logger log = LoggerFactory.getLogger(GetDeadLockedPhrasesFunction.class);

  public GetDeadLockedPhrasesFunction(List<GroupedPhraseVoteWithMostRecentVoteTime> phraseVotes, Set<String> phrasesVotedOnByResolver, int consensusLevel)
  {
    this.phraseVotes = phraseVotes;
    this.phrasesVotedOnByResolver = phrasesVotedOnByResolver;
    this.consensusLevel = consensusLevel;
  }

  public List<DeadLockedPhrase> execute(int userCount)
  {
    Map<String, List<LabelCount>> groupedPhraseVotes = phraseVotes.stream()
        .filter(phrase -> !phrasesVotedOnByResolver.contains(phrase.getPhraseId()))
        .collect(Collectors.groupingBy(GroupedPhraseVote::getText,
            Collectors.mapping(phraseVote -> new LabelCount(phraseVote.getLabel(), phraseVote.getVoteCount()), Collectors.toList())));

    // Assumes the SQL query got the same timestamp for each phrase
    Map<String, Timestamp> mostRecentVoteTimeForPhrases = phraseVotes.stream()
        .collect(Collectors.toMap(GroupedPhraseVote::getText, GroupedPhraseVoteWithMostRecentVoteTime::getMaxTime, (a, b) -> a));

    List<DeadLockedPhrase> deadLockedPhrases = groupedPhraseVotes.entrySet().stream()
        .filter((e) -> isDeadLocked(e.getValue(), consensusLevel, userCount))
        .map((e) -> {
          Phrase phrase = new Phrase(e.getKey());
          LocalDateTime dateTime = mostRecentVoteTimeForPhrases.get(e.getKey()).toLocalDateTime();
          List<LabelCount> labels = e.getValue();
          return new DeadLockedPhrase(phrase, labels, dateTime);
        })
        .sorted(Comparator.comparing(DeadLockedPhrase::getMostRecentVoteTime).reversed())
        .collect(Collectors.toList());

    log.info("Found {} deadlocked phrase(s).", deadLockedPhrases.size());
    return deadLockedPhrases;
  }

  private boolean isDeadLocked(List<LabelCount> labelCounts, int consensusLevel, int userCount)
  {
    if (labelCounts.size() < 2)
    {
      return false;
    }
    labelCounts.sort(Comparator.comparingInt(LabelCount::getCount).reversed());

    int highestLabelVoteCount = labelCounts.get(0).getCount();
    int secondHighestLabelVoteCount = labelCounts.get(1).getCount();
    int totalVotes = labelCounts.stream().mapToInt(LabelCount::getCount).sum();

    int voteDifferenceBetweenTop2Labels = highestLabelVoteCount - secondHighestLabelVoteCount;
    int possibleRemainingVotes = userCount - totalVotes;

    return possibleRemainingVotes + voteDifferenceBetweenTop2Labels < consensusLevel;
  }
}
