package com.rabidgremlin.concord.resources;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.rabidgremlin.concord.api.UserStats;
import com.rabidgremlin.concord.api.UserVoteCount;
import com.rabidgremlin.concord.auth.Caller;
import com.rabidgremlin.concord.dao.StatsDao;

public class StatsResourceTest
{

  @Mock
  private StatsDao statsDao;

  @Mock
  private Caller caller;

  private StatsResource statsResource;

  @Before
  public void setup()
  {
    MockitoAnnotations.initMocks(this);
    statsResource = new StatsResource(statsDao, 3);
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
    when(statsDao.getTotalCountOfVotesMadePerUser()).thenReturn(totalCounts);
    when(statsDao.getCompletedCountOfVotesMadePerUser()).thenReturn(completedCounts);
    when(statsDao.getCountOfTrashVotesPerUser()).thenReturn(trashedCounts);
    when(statsDao.getCountOfVotesMadePerUserForPhrasesBeyondVoteMargin(anyInt())).thenReturn(totalCountsBeyondConsensus);

    // When
    Response response = statsResource.getUserStats(caller);

    // Then
    List<UserStats> expectedStats = Arrays.asList(
        new UserStats("user2", 100, 39, 23, 0.5f, 0.23f),
        new UserStats("user3", 70, 7, 0, 0.7f, 0),
        new UserStats("user1", 10, 2, 1, 0.2f, 0.1f));
    assertThat(response, instanceOf(Response.class));
    assertEquals(200, response.getStatus());
    assertEquals("OK", response.getStatusInfo().toString());
    assertThat(response.getEntity(), instanceOf(List.class));
    assertEquals(expectedStats, response.getEntity());
  }

  @Test
  public void shouldGetEmptyListWhenNoVotesMade()
  {
    // When
    Response response = statsResource.getUserStats(caller);

    // Then
    assertThat(response, instanceOf(Response.class));
    assertEquals(200, response.getStatus());
    assertEquals("OK", response.getStatusInfo().toString());
    assertThat(response.getEntity(), instanceOf(List.class));
    assertEquals(0, ((List) response.getEntity()).size());
  }

  @Test
  public void shouldFilterOutBulkUploadUser()
  {
    // Given
    List<UserVoteCount> dummyVotes = Collections.singletonList(new UserVoteCount("BULK_UPLOAD", 9999));
    when(statsDao.getTotalCountOfVotesMadePerUser()).thenReturn(dummyVotes);

    // When
    Response response = statsResource.getUserStats(caller);

    // Then
    assertThat(response, instanceOf(Response.class));
    assertEquals(200, response.getStatus());
    assertEquals("OK", response.getStatusInfo().toString());
    assertThat(response.getEntity(), instanceOf(List.class));
    assertEquals(0, ((List) response.getEntity()).size());
  }

}
