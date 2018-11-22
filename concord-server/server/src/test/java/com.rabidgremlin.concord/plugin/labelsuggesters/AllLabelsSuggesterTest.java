package com.rabidgremlin.concord.plugin.labelsuggesters;

import com.rabidgremlin.concord.plugin.SuggestedLabel;
import com.rabidgremlin.concord.plugin.SystemLabel;
import com.rabidgremlin.concord.plugin.SystemLabelStore;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AllLabelsSuggesterTest
{
    private AllLabelsSuggester labelSuggester;
    private String phrase;
    private List<SystemLabel> systemLabels = new ArrayList<>();

    @Mock
    SystemLabelStore labelStoreMock;

    public void setUp()
    {
        SystemLabel label1 = new SystemLabel("TupacQuote", "Tupac quote", "A quote by Tupac.");
        SystemLabel label2 = new SystemLabel("HillaryQuote", "Hillary quote", "A quote by Hillary");
        SystemLabel label3 = new SystemLabel("GandhiQuote", "Gandhi quote", "A quote by Gandhi");

        systemLabels.add(label1);
        systemLabels.add(label2);
        systemLabels.add(label3);

        phrase = "Reality is wrong, dreams are for real.";
        labelStoreMock = mock(SystemLabelStore.class);
        labelSuggester = new AllLabelsSuggester(labelStoreMock);

    }

    // WIP
//    public void canReturnSuggestedLabels()
//    {
//        when(labelStoreMock.getSystemLabels()).thenReturn(systemLabels);
//
//        List<SuggestedLabel> suggestedLabels = labelSuggester.suggestLabels(phrase);
//    }

}
