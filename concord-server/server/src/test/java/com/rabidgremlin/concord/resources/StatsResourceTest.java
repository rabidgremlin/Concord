package com.rabidgremlin.concord.resources;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.rabidgremlin.concord.api.SystemStats;
import com.rabidgremlin.concord.api.UserStats;
import com.rabidgremlin.concord.api.UserVoteCount;
import com.rabidgremlin.concord.auth.Caller;
import com.rabidgremlin.concord.dao.SystemStatsDao;
import com.rabidgremlin.concord.dao.UserStatsDao;

public class StatsResourceTest
{

  @Mock
  private UserStatsDao userStatsDao;

  @Mock
  private SystemStatsDao systemStatsDao;

  @Mock
  private Caller caller;

  private StatsResource statsResource;

  @Before
  public void setup()
  {
    MockitoAnnotations.initMocks(this);
    statsResource = new StatsResource(userStatsDao, systemStatsDao, 0);
  }

  @Test
  public void canGetUserStatsOrderedByTotalVotes()
  {
    // Given
    List<UserVoteCount> totalCounts = Arrays.asList(
        new UserVoteCount("user1", 10),
        new UserVoteCount("user2", 100),
        new UserVoteCount("user3", 70));
    List<UserVoteCount> completedCounts = Arrays.asList(
        new UserVoteCount("user1", 2),
        new UserVoteCount("user2", 39),
        new UserVoteCount("user3", 7));
    List<UserVoteCount> trashedCounts = Arrays.asList(
        new UserVoteCount("user1", 1),
        new UserVoteCount("user2", 23),
        new UserVoteCount("user3", 0));
    List<UserVoteCount> totalCountsBeyondConsensus = Arrays.asList(
        new UserVoteCount("user1", 10),
        new UserVoteCount("user2", 78),
        new UserVoteCount("user3", 10));
    List<UserVoteCount> dummyScores = Arrays.asList(
        new UserVoteCount("user1", 0),
        new UserVoteCount("user2", 0),
        new UserVoteCount("user3", 0));
    when(userStatsDao.getCountOfTotalVotesPerUser()).thenReturn(totalCounts);
    when(userStatsDao.getCountOfCompletedVotesPerUser()).thenReturn(completedCounts);
    when(userStatsDao.getCountOfTrashVotesPerUser()).thenReturn(trashedCounts);
    when(userStatsDao.getCountOfTotalVotesWithConsensusPerUser()).thenReturn(totalCountsBeyondConsensus);
    when(userStatsDao.getCountOfCompletedVotesPerUserIgnoringTrash()).thenReturn(dummyScores);
    when(userStatsDao.getCountOfTotalVotesWithConsensusPerUserIgnoringTrash()).thenReturn(dummyScores);

    // When
    Response response = statsResource.getUserStats(caller);

    // Then
    List<UserStats> expectedStats = Arrays.asList(
        new UserStats("user1", 10, 2, 1, 10, 0, 0),
        new UserStats("user2", 100, 39, 23, 78, 0, 0),
        new UserStats("user3", 70, 7, 0, 10, 0, 0));
    assertThat(response, instanceOf(Response.class));
    assertThat(response.getStatus(), is(200));
    assertThat(response.getStatusInfo().toString(), is("OK"));
    assertThat(response.getEntity(), instanceOf(List.class));
    assertThat(response.getEntity(), is(expectedStats));
  }

  @Test
  public void shouldGetEmptyListWhenNoVotesMade()
  {
    // When
    Response response = statsResource.getUserStats(caller);

    // Then
    assertThat(response, instanceOf(Response.class));
    assertThat(response.getStatus(), is(200));
    assertThat(response.getStatusInfo().toString(), is("OK"));
    assertThat(response.getEntity(), instanceOf(List.class));
    assertThat(((List) response.getEntity()).size(), is(0));
  }

  @Test
  @Ignore("Decided to keep BULK_UPLOAD in the stats.")
  public void shouldFilterOutBulkUploadUser()
  {
    // Given
    List<UserVoteCount> dummyVotes = Collections.singletonList(new UserVoteCount("BULK_UPLOAD", 9999));
    when(userStatsDao.getCountOfTotalVotesPerUser()).thenReturn(dummyVotes);

    // When
    Response response = statsResource.getUserStats(caller);

    // Then
    assertThat(response, instanceOf(Response.class));
    assertThat(response.getStatus(), is(200));
    assertThat(response.getStatusInfo().toString(), is("OK"));
    assertThat(response.getEntity(), instanceOf(List.class));
    assertThat(((List) response.getEntity()).size(), is(0));
  }

  @Test
  public void shouldGetSystemStats()
  {
    // Given
    when(systemStatsDao.getTotalCountOfPhrases()).thenReturn(100);
    when(systemStatsDao.getCountOfCompletedPhrases()).thenReturn(50);
    when(systemStatsDao.getCountOfPhrasesWithConsensusThatAreNotCompleted(anyInt())).thenReturn(25);

    // When
    Response response = statsResource.getSystemStats(caller);

    // Then
    assertThat(response, instanceOf(Response.class));
    assertThat(response.getStatus(), is(200));
    assertThat(response.getStatusInfo().toString(), is("OK"));
    assertThat(response.getEntity(), instanceOf(SystemStats.class));
    assertThat(response.getEntity(), is(new SystemStats(100, 50, 25)));
  }

}
