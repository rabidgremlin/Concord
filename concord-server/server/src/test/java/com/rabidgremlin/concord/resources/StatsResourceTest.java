package com.rabidgremlin.concord.resources;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response;

import com.rabidgremlin.concord.api.UserVoteCount;
import com.rabidgremlin.concord.dao.StatsDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.rabidgremlin.concord.auth.Caller;

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
  public void shouldGetUserScoresWhenAvailable()
  {
    // Given
    List<UserVoteCount> dummyScores = Arrays.asList(
        new UserVoteCount("user1", 10),
        new UserVoteCount("user2", 100),
        new UserVoteCount("user3", 9999));
    when(statsDao.getTotalCountOfVotesMadePerUser()).thenReturn(dummyScores);

    // When
    Response response = statsResource.getTotalUserVotes(caller);

    // Then
    assertThat(response, instanceOf(Response.class));
    assertEquals(200, response.getStatus());
    assertEquals("OK", response.getStatusInfo().toString());
    assertThat(response.getEntity(), instanceOf(List.class));
    assertEquals(response.getEntity(), dummyScores);
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
    assertEquals(((List) response.getEntity()).size(), 0);
  }

  @Test
  public void shouldFilterOutBulkUploadUser()
  {
    // Given
    List<UserVoteCount> dummyScores = Arrays.asList(
        new UserVoteCount("user1", 10),
        new UserVoteCount("user2", 100));
    List<UserVoteCount> dummyScoresWithBulkUpload = new ArrayList<>(dummyScores);
    dummyScoresWithBulkUpload.add(new UserVoteCount("BULK_UPLOAD", 9999));
    when(statsDao.getTotalCountOfVotesMadePerUser()).thenReturn(dummyScoresWithBulkUpload);

    // When
    Response response = statsResource.getTotalUserVotes(caller);

    // Then
    assertThat(response, instanceOf(Response.class));
    assertEquals(200, response.getStatus());
    assertEquals("OK", response.getStatusInfo().toString());
    assertThat(response.getEntity(), instanceOf(List.class));
    assertEquals(response.getEntity(), dummyScores);
  }

}
