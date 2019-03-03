package com.rabidgremlin.concord.resources;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.rabidgremlin.concord.api.UserVoteCount;
import com.rabidgremlin.concord.api.UserVoteRatio;
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
    statsResource = new StatsResource(statsDao);
  }

  @Test
  public void getGetTotalUserVotes()
  {
    // Given
    List<UserVoteCount> dummyVotes = Arrays.asList(
        new UserVoteCount("user1", 10),
        new UserVoteCount("user2", 100),
        new UserVoteCount("user3", 9999));
    when(statsDao.getTotalCountOfVotesMadePerUser()).thenReturn(dummyVotes);

    // When
    Response response = statsResource.getTotalUserVotes(caller);

    // Then
    List<UserVoteCount> expectedVotes = Arrays.asList(
        new UserVoteCount("user1", 10),
        new UserVoteCount("user2", 100),
        new UserVoteCount("user3", 9999));
    assertThat(response, instanceOf(Response.class));
    assertEquals(200, response.getStatus());
    assertEquals("OK", response.getStatusInfo().toString());
    assertThat(response.getEntity(), instanceOf(List.class));
    assertEquals(expectedVotes, response.getEntity());
  }

  @Test
  public void shouldGetEmptyListWhenNoVotesMade()
  {
    // When
    Response response = statsResource.getTotalUserVotes(caller);

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
    List<UserVoteCount> dummyVotes = Arrays.asList(
        new UserVoteCount("user1", 10),
        new UserVoteCount("user2", 100),
        new UserVoteCount("BULK_UPLOAD", 9999));
    when(statsDao.getTotalCountOfVotesMadePerUser()).thenReturn(dummyVotes);

    // When
    Response response = statsResource.getTotalUserVotes(caller);

    // Then
    List<UserVoteCount> expectedVotes = Arrays.asList(
        new UserVoteCount("user1", 10),
        new UserVoteCount("user2", 100));
    assertThat(response, instanceOf(Response.class));
    assertEquals(200, response.getStatus());
    assertEquals("OK", response.getStatusInfo().toString());
    assertThat(response.getEntity(), instanceOf(List.class));
    assertEquals(expectedVotes, response.getEntity());
  }

  @Test
  public void canGetCompletedUserVotes()
  {
    // Given
    List<UserVoteCount> dummyVotes = Arrays.asList(
        new UserVoteCount("user1", 10),
        new UserVoteCount("user2", 100),
        new UserVoteCount("user3", 9999));
    when(statsDao.getCompletedCountOfVotesMadePerUser()).thenReturn(dummyVotes);

    // When
    Response response = statsResource.getCompletedUserVotes(caller);

    // Then
    List<UserVoteCount> expectedVotes = Arrays.asList(
        new UserVoteCount("user1", 10),
        new UserVoteCount("user2", 100),
        new UserVoteCount("user3", 9999));
    assertThat(response, instanceOf(Response.class));
    assertEquals(200, response.getStatus());
    assertEquals("OK", response.getStatusInfo().toString());
    assertThat(response.getEntity(), instanceOf(List.class));
    assertEquals(expectedVotes, response.getEntity());
  }

  @Test
  public void canGetRatioOfCompletedUserVotes()
  {
    // Given
    List<UserVoteCount> dummyTotalVotes = Arrays.asList(
        new UserVoteCount("user1", 10),
        new UserVoteCount("user2", 100),
        new UserVoteCount("user3", 400));
    List<UserVoteCount> dummyCompletedVotes = Arrays.asList(
        new UserVoteCount("user1", 1),
        new UserVoteCount("user2", 37),
        new UserVoteCount("user3", 50));
    when(statsDao.getCountOfVotesMadePerUserForPhrasesBeyondVoteMargin(anyInt())).thenReturn(dummyTotalVotes);
    when(statsDao.getCompletedCountOfVotesMadePerUser()).thenReturn(dummyCompletedVotes);

    // When
    Response response = statsResource.getCompletedRatioOfUserVotesForPhrasesBeyondConsensus(caller);

    // Then
    List<UserVoteRatio> expectedRatios = Arrays.asList(
        new UserVoteRatio("user2", 0.37d),
        new UserVoteRatio("user3", 0.125d),
        new UserVoteRatio("user1", 0.1d));

    assertThat(response, instanceOf(Response.class));
    assertEquals(200, response.getStatus());
    assertEquals("OK", response.getStatusInfo().toString());
    assertThat(response.getEntity(), instanceOf(List.class));
    assertEquals(expectedRatios, response.getEntity());
  }

}
