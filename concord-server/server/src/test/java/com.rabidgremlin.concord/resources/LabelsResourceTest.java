package com.rabidgremlin.concord.resources;

import com.rabidgremlin.concord.api.Label;
import com.rabidgremlin.concord.auth.Caller;
import com.rabidgremlin.concord.dao.LabelsDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LabelsResourceTest
{
    private LabelsResource labelsResource;

    private List<Label> labels = new ArrayList<>();

    @Mock
    Caller callerMock;

    @Mock
    LabelsDao labelsDaoMock;

    @Before
    public void setUp()
    {
        labelsDaoMock = mock(LabelsDao.class);
        callerMock = mock(Caller.class);
        labelsResource = new LabelsResource(labelsDaoMock);

        Label label1 = new Label();
        label1.setLabel("OrderTaxi");
        label1.setShortDescription("Order Taxi");
        label1.setLongDescription("Where would you like your taxi to be sent ?");

        labels.add(label1);
    }

    @Test
    public void canUploadCSVOfLabels()
    {
        Response response = labelsResource.uploadCsv(callerMock, labels);

        verify(labelsDaoMock, times(1)).upsert(any());
        assertThat(response, instanceOf(Response.class));
        assertEquals(200,response.getStatus());
        assertEquals("OK",response.getStatusInfo().toString());
    }
}
