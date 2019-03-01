package com.rabidgremlin.concord.resources;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.rabidgremlin.concord.api.UserVotesMade;
import com.rabidgremlin.concord.dao.VotesDao;

public class UsersResourceTest
{

  @Mock
  private VotesDao votesDao;

  private UsersResource usersResource;

  @Before
  public void setup()
  {
    MockitoAnnotations.initMocks(this);
    usersResource = new UsersResource(votesDao);
  }

  @Test
  public void shouldGetUserScoresWhenAvailable()
  {
    // Given
    List<UserVotesMade> dummyScores = Arrays.asList(
        new UserVotesMade("user1", 10),
        new UserVotesMade("user2", 100),
        new UserVotesMade("user3", 9999));
    when(votesDao.getVotesMadePerUser()).thenReturn(dummyScores);

    // When
    Response response = usersResource.getUserScores();

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
    Response response = usersResource.getUserScores();

    // Then
    assertThat(response, instanceOf(Response.class));
    assertEquals(200, response.getStatus());
    assertEquals("OK", response.getStatusInfo().toString());
    assertThat(response.getEntity(), instanceOf(List.class));
    assertEquals(((List) response.getEntity()).size(), 0);
  }

}
