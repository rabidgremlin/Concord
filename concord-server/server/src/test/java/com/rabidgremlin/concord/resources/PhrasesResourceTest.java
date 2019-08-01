package com.rabidgremlin.concord.resources;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.rabidgremlin.concord.api.Label;
import com.rabidgremlin.concord.api.Phrase;
import com.rabidgremlin.concord.api.PhraseToLabel;
import com.rabidgremlin.concord.api.UnlabelledPhrase;
import com.rabidgremlin.concord.api.UnlabelledPhrases;
import com.rabidgremlin.concord.auth.Caller;
import com.rabidgremlin.concord.dao.PhrasesDao;
import com.rabidgremlin.concord.dao.UploadDao;
import com.rabidgremlin.concord.dao.VotesDao;
import com.rabidgremlin.concord.plugin.LabelSuggester;
import com.rabidgremlin.concord.plugin.SuggestedLabel;
import com.rabidgremlin.concord.plugin.UnableToGetSuggestionsException;

public class PhrasesResourceTest
{

  private PhrasesResource resource;

  private final List<SuggestedLabel> labels = new ArrayList<>();

  private final List<UnlabelledPhrase> unlabelledPhrases = new ArrayList<>();

  @Mock
  private PhrasesDao phrasesDaoMock;

  @Mock
  private Caller callerMock;

  @Mock
  private VotesDao votesDaoMock;

  @Mock
  private LabelSuggester labelSuggestMock;

  @Mock
  private UploadDao uploadDaoMock;

  @Mock
  private UriInfo uriInfo;

  @Before
  public void setUp()
  {
    MockitoAnnotations.initMocks(this);

    resource = new PhrasesResource(phrasesDaoMock, votesDaoMock, uploadDaoMock, labelSuggestMock, 1, false);
    resource.uriInfo = uriInfo;

    SuggestedLabel label1 = new SuggestedLabel("WhereTaxi", "Where Taxi", "Your taxi is ... minutes away.", 0.5);
    SuggestedLabel label2 = new SuggestedLabel("CancelTaxi", "Cancel Taxi", "Ok your Taxi has been ordered", 0.25);
    labels.add(label1);
    labels.add(label2);

    UnlabelledPhrase phrase1 = new UnlabelledPhrase();
    phrase1.setText("how can I order a taxi");
    phrase1.setPossibleLabel("OrderTaxi");

    UnlabelledPhrase phrase2 = new UnlabelledPhrase();
    phrase2.setText("what is the meaning of life?");

    UnlabelledPhrase phrase3 = new UnlabelledPhrase();
    phrase3.setText("text");

    unlabelledPhrases.add(phrase1);
    unlabelledPhrases.add(phrase2);
    unlabelledPhrases.add(phrase3);
  }

  @Test
  public void shouldGetNextPhraseWhenPhraseIsAvailable()
    throws UnableToGetSuggestionsException
  {
    Phrase testPhrase = new Phrase();
    testPhrase.setText("Where is my taxi?");
    testPhrase.setPhraseId("1234");

    when(phrasesDaoMock.getNextPhraseToLabelForUser(anyString())).thenReturn(testPhrase);
    when(labelSuggestMock.suggestLabels(anyString())).thenReturn(labels);
    when(callerMock.getToken()).thenReturn("010");

    Response response = resource.getNextPhraseToLabel(callerMock);

    assertThat(response, instanceOf(Response.class));
    assertThat(response.getEntity(), instanceOf(PhraseToLabel.class));
    assertEquals(200, response.getStatus());
    assertEquals("OK", response.getStatusInfo().toString());
  }

  @Test
  public void shouldReturnResponseWhenNextPhraseIsNotAvailable()
  {
    when(phrasesDaoMock.getNextPhraseToLabelForUser(anyString())).thenReturn(null);
    when(callerMock.getToken()).thenReturn("010");

    Response response = resource.getNextPhraseToLabel(callerMock);

    assertThat(response, instanceOf(Response.class));
    assertEquals(404, response.getStatus());
    assertEquals("Not Found", response.getStatusInfo().toString());
  }

  @Test
  public void canUploadPhrasesAsCsv()
  {
    Response response = resource.uploadCsv(callerMock, "false", unlabelledPhrases);

    verify(uploadDaoMock, times(1)).uploadUnlabelledPhrases(unlabelledPhrases, UploadDao.ExistingVotes.RETAIN);

    assertThat(response, instanceOf(Response.class));
    assertEquals(201, response.getStatus());
    assertEquals("Created", response.getStatusInfo().toString());
  }

  @Test
  public void canUploadPhrasesAsCsvWithQueryParam()
  {
    Response response = resource.uploadCsv(callerMock, "true", unlabelledPhrases);

    verify(uploadDaoMock, times(1)).uploadUnlabelledPhrases(unlabelledPhrases, UploadDao.ExistingVotes.REPLACE);

    assertThat(response, instanceOf(Response.class));
    assertEquals(201, response.getStatus());
    assertEquals("Created", response.getStatusInfo().toString());
  }

  @Test
  public void canUploadPhrasesAsJson()
  {
    Response response = resource.addPhrases(callerMock, "false", new UnlabelledPhrases(unlabelledPhrases));

    verify(uploadDaoMock, times(1)).uploadUnlabelledPhrases(unlabelledPhrases, UploadDao.ExistingVotes.RETAIN);

    assertThat(response, instanceOf(Response.class));
    assertEquals(201, response.getStatus());
    assertEquals("Created", response.getStatusInfo().toString());
  }

  @Test
  public void canUploadPhrasesAsJsonWithQueryParam()
  {
    Response response = resource.addPhrases(callerMock, "True", new UnlabelledPhrases(unlabelledPhrases));

    verify(uploadDaoMock, times(1)).uploadUnlabelledPhrases(unlabelledPhrases, UploadDao.ExistingVotes.REPLACE);

    assertThat(response, instanceOf(Response.class));
    assertEquals(201, response.getStatus());
    assertEquals("Created", response.getStatusInfo().toString());
  }

  @Test
  public void shouldMarkPhraseCompletedWhenTrashedAndCompleteOnFirstTrashVoteIsTrue()
  {
    when(callerMock.getToken()).thenReturn("Bob");

    resource = new PhrasesResource(phrasesDaoMock, votesDaoMock, uploadDaoMock, labelSuggestMock, 1, true);
    resource.uriInfo = mock(UriInfo.class);

    String trashedPhraseId = "abcdefg1234567";
    Label trashedLabel = new Label("TRASH", "", "");

    Response response = resource.voteForPhrase(callerMock, trashedPhraseId, trashedLabel);

    verify(votesDaoMock, times(1)).upsert(trashedPhraseId, "TRASH", "Bob");
    verify(phrasesDaoMock, times(1)).markPhraseComplete(trashedPhraseId, "TRASH");

    assertThat(response, instanceOf(Response.class));
    assertEquals(201, response.getStatus());
    assertEquals("Created", response.getStatusInfo().toString());
  }

  @Test
  public void shouldNotMarkPhraseCompletedWhenTrashedAndCompleteOnFirstTrashVoteIsTrue()
  {
    when(callerMock.getToken()).thenReturn("Bob");

    String trashedPhraseId = "abcdefg1234567";
    Label trashedLabel = new Label("TRASH", "", "");

    Response response = resource.voteForPhrase(callerMock, trashedPhraseId, trashedLabel);

    verify(votesDaoMock, times(1)).upsert(trashedPhraseId, "TRASH", "Bob");
    verify(phrasesDaoMock, times(0)).markPhrasesComplete(Collections.singletonList(trashedPhraseId), Collections.singletonList("TRASH"));
    verify(phrasesDaoMock, times(0)).markPhraseComplete(trashedPhraseId, "TRASH");

    assertThat(response, instanceOf(Response.class));
    assertEquals(201, response.getStatus());
    assertEquals("Created", response.getStatusInfo().toString());
  }

  @Test
  public void canVoteForPhrase()
  {
    when(callerMock.getToken()).thenReturn("caller");

    Response response = resource.voteForPhrase(callerMock, "123", new Label("WhereTaxi", "", ""));

    verify(votesDaoMock, times(1)).upsert("123", "WhereTaxi", "caller");
    verify(phrasesDaoMock, times(0)).markPhrasesComplete(Collections.singletonList("123"), Collections.singletonList("WhereTaxi"));
    verify(phrasesDaoMock, times(0)).markPhraseComplete("123", "WhereTaxi");

    assertThat(response, instanceOf(Response.class));
    assertThat(response.getStatus(), is(201));
    assertThat(response.getStatusInfo().toString(), is("Created"));
  }

  @Test
  public void canResolvePhrase()
  {
    Response response = resource.resolvePhrase(callerMock, "123", new Label("WhereTaxi", "", ""));

    verify(votesDaoMock, times(1)).upsert("123", "WhereTaxi", "RESOLVER");
    verify(phrasesDaoMock, times(0)).markPhrasesComplete(Collections.singletonList("123"), Collections.singletonList("WhereTaxi"));
    verify(phrasesDaoMock, times(0)).markPhraseComplete("123", "WhereTaxi");

    assertThat(response, instanceOf(Response.class));
    assertThat(response.getStatus(), is(201));
    assertThat(response.getStatusInfo().toString(), is("Created"));
  }

  @Test
  public void canDeleteVotesForPhrase()
  {
    Response response = resource.purgeVotesForPhrase(callerMock, "123");

    verify(votesDaoMock, times(1)).deleteAllVotesForPhrase(Collections.singletonList("123"));

    assertThat(response, instanceOf(Response.class));
    assertThat(response.getStatus(), is(200));
    assertThat(response.getStatusInfo().toString(), is("OK"));
  }

  @Test
  public void canDeleteSpecifiedPhrase()
  {
    when(callerMock.getToken()).thenReturn("caller");

    Response response = resource.deleteVote(callerMock, "123");

    verify(votesDaoMock, times(1)).deleteVote("123", "caller");

    assertThat(response, instanceOf(Response.class));
    assertThat(response.getStatus(), is(204));
    assertThat(response.getStatusInfo().toString(), is("No Content"));
  }

}
