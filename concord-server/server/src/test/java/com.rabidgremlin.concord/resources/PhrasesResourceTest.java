package com.rabidgremlin.concord.resources;

import com.rabidgremlin.concord.api.Label;
import com.rabidgremlin.concord.api.Phrase;
import com.rabidgremlin.concord.api.PhraseToLabel;
import com.rabidgremlin.concord.api.UnlabelledPhrase;
import com.rabidgremlin.concord.auth.Caller;
import com.rabidgremlin.concord.dao.PhrasesDao;
import com.rabidgremlin.concord.dao.VotesDao;
import com.rabidgremlin.concord.plugin.LabelSuggester;
import com.rabidgremlin.concord.plugin.SuggestedLabel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;


import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PhrasesResourceTest
{

    private PhrasesResource resource;

    private List<SuggestedLabel> labels = new ArrayList<>();

    private List<UnlabelledPhrase> unlabelledPhrases = new ArrayList<>();


    @Mock
    PhrasesDao phrasesDaoMock;

    @Mock
    Caller callerMock;

    @Mock
    VotesDao votesDaoMock;

    @Mock
    LabelSuggester labelSuggestMock;

    @Before
    public void setUp()
    {
        phrasesDaoMock = mock(PhrasesDao.class);
        votesDaoMock = mock(VotesDao.class);
        labelSuggestMock = mock(LabelSuggester.class);
        callerMock = mock(Caller.class);
        resource = new PhrasesResource(phrasesDaoMock, votesDaoMock, labelSuggestMock);

        SuggestedLabel label1 = new SuggestedLabel("WhereTaxi","Where Taxi","Your taxi is ... minutes away.", 0.5);
        SuggestedLabel label2 = new SuggestedLabel("CancelTaxi","Cancel Taxi","Ok your Taxi has been ordered", 0.25);
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

//    // WIP
//    @Test
//    public void canVoteForAPhrase()
//    {
//        String phraseId = "1234";
//        Label label = new Label();
//        label.setLabel("OrderTaxi");
//        label.setShortDescription("Order Taxi");
//        label.setLongDescription("Where would you like your taxi to be sent ?");
//
//        Response response = resource.voteForPhrase(callerMock, phraseId, label);
//    }
//
//    // WIP
//    @Test
//    public void canUploadCSVOfPhrases()
//    {
//        Response response = resource.uploadCsv(callerMock, unlabelledPhrases);
//
//    }
//
//    // WIP
//    @Test
//    public void canDownloadCSVOfPhrases()
//    {
//        Response response = resource.downloadCsv(callerMock);
//    }

}


