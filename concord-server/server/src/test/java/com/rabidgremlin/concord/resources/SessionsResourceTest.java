package com.rabidgremlin.concord.resources;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.rabidgremlin.concord.api.NewSessionRequest;
import com.rabidgremlin.concord.plugin.CredentialsValidator;

public class SessionsResourceTest
{
  private SessionsResource sessionsResource;

  private SessionsResource badSessionsResource;

  @Mock
  private CredentialsValidator validatorMock;

  @Mock
  private NewSessionRequest sessionRequestMock;

  @Before
  public void setUp()
  {
    MockitoAnnotations.initMocks(this);

    byte[] token = "123456789abcdefghijklmnopqrstuvwxyz".getBytes();
    sessionsResource = new SessionsResource(token, validatorMock);

    byte[] badToken = "peanutBrittle".getBytes();
    badSessionsResource = new SessionsResource(badToken, validatorMock);
  }

  @Test
  public void canGenerateExpiredTokenSuccessfully()
  {
    when(validatorMock.validateCredentials(any(), any())).thenReturn(true);
    when(sessionRequestMock.getUserId()).thenReturn("wookie12");
    when(sessionRequestMock.getPassword()).thenReturn("cookies");

    Response response = sessionsResource.generateExpiredToken(sessionRequestMock);

    assertThat(response, instanceOf(Response.class));

    assertEquals(200, response.getStatus());
    assertEquals("OK", response.getStatusInfo().toString());
  }

  @Test
  public void canGenerateExpiredTokenWithUnauthorizedAccess()
  {
    when(validatorMock.validateCredentials(any(), any())).thenReturn(false);

    Response response = sessionsResource.generateExpiredToken(sessionRequestMock);

    assertThat(response, instanceOf(Response.class));
    assertEquals(401, response.getStatus());
    assertEquals("Unauthorized", response.getStatusInfo().toString());
  }

  @Test
  public void canGenerateExpiredTokenWithServerError()
  {
    when(validatorMock.validateCredentials(any(), any())).thenReturn(true);
    when(sessionRequestMock.getUserId()).thenReturn("wookie12");
    when(sessionRequestMock.getPassword()).thenReturn("cookies");

    Response response = badSessionsResource.generateExpiredToken(sessionRequestMock);

    assertThat(response, instanceOf(Response.class));
    assertEquals(500, response.getStatus());
    assertEquals("Internal Server Error", response.getStatusInfo().toString());
  }
}
