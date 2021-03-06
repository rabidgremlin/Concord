package com.rabidgremlin.concord.plugin.labelsuggesters;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.rabidgremlin.concord.plugin.SuggestedLabel;
import com.rabidgremlin.concord.plugin.SystemLabel;
import com.rabidgremlin.concord.plugin.SystemLabelStore;

public class AllLabelsSuggesterTest
{
  private AllLabelsSuggester labelSuggester;

  private String phrase;

  private final List<SystemLabel> systemLabels = new ArrayList<>();

  @Mock
  private SystemLabelStore labelStoreMock;

  @Before
  public void setUp()
    throws Exception
  {
    SystemLabel label1 = new SystemLabel("TupacQuote", "Tupac quote", "A quote by Tupac");
    SystemLabel label2 = new SystemLabel("HillaryQuote", "Hillary quote", "A quote by Hillary");
    SystemLabel label3 = new SystemLabel("GandhiQuote", "Gandhi quote", "A quote by Gandhi");

    systemLabels.add(label1);
    systemLabels.add(label2);
    systemLabels.add(label3);

    phrase = "Reality is wrong, dreams are for real.";
    labelStoreMock = mock(SystemLabelStore.class);
    labelSuggester = new AllLabelsSuggester(labelStoreMock, new HashMap<>());
  }

  @Test
  public void canReturnSuggestedLabels()
  {
    Double expectedScore = 1.0d / (double) systemLabels.size();

    when(labelStoreMock.getSystemLabels()).thenReturn(systemLabels);

    List<SuggestedLabel> suggestedLabels = labelSuggester.suggestLabels(phrase);

    assertEquals(3, suggestedLabels.size());
    assertEquals("TupacQuote", suggestedLabels.get(0).getLabel());
    assertEquals(expectedScore, suggestedLabels.get(0).getScore());
    assertEquals("Tupac quote", suggestedLabels.get(0).getShortDescription());
    assertEquals("A quote by Tupac", suggestedLabels.get(0).getLongDescription());
  }

}
