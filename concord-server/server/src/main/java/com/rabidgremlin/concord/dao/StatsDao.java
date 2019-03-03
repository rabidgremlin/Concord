package com.rabidgremlin.concord.dao;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import com.rabidgremlin.concord.api.UserVoteCount;

public interface StatsDao
{

  /**
   * Returns the raw total count of votes made per user.
   */
  @SqlQuery("select userId, count(*) voteCount from votes group by userId order by voteCount desc")
  @RegisterBeanMapper(UserVoteCount.class)
  List<UserVoteCount> getTotalCountOfVotesMadePerUser();

  /*
   * TODO filter out SKIP and TRASH ???? They are considered complete with less than consensus level.
   */

  /**
   * Returns the count of votes made per user, for phrases which have been marked complete with the same label the user
   * voted for.
   */
  @SqlQuery("select userId, count(*) voteCount from votes join phrases on phrases.phraseId=votes.phraseId where completed = true AND phrases.label=votes.label group by userId order by voteCount desc")
  @RegisterBeanMapper(UserVoteCount.class)
  List<UserVoteCount> getCompletedCountOfVotesMadePerUser();

  /**
   * Returns the total count of votes made per user, for phrases which have been voted on more or equal times of the
   * margin.
   *
   * @param margin the amount of times a phrase must be voted on to be considered in the query. Recommend setting to the
   *          consensus level.
   */
  @SqlQuery("select userId, count(*) voteCount from votes where phraseId in (select phraseId from votes group by phraseId having count(phraseId) >= :margin) group by userId order by voteCount desc")
  @RegisterBeanMapper(UserVoteCount.class)
  List<UserVoteCount> getCountOfVotesMadePerUserForPhrasesBeyondVoteMargin(@Bind("margin") int margin);

}
