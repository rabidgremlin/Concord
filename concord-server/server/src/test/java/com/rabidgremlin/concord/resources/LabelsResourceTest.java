package com.rabidgremlin.concord.resources;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.rabidgremlin.concord.api.Label;
import com.rabidgremlin.concord.auth.Caller;

public class LabelsResourceTest
    extends BaseResourceTest
{
  private LabelsResource labelsResource;

  @Mock
  private Caller callerMock;

  @Before
  public void setUp()
    throws Exception
  {
    MockitoAnnotations.initMocks(this);

    setUpDatabase();

    labelsResource = createLabelsResource();
  }

  @Test
  public void canUploadCSVOfLabels()
  {
    // given this list of labels
    List<Label> labels = new ArrayList<>();
    Label label1 = new Label();
    label1.setLabel("OrderTaxi");
    label1.setShortDescription("Order Taxi");
    label1.setLongDescription("Where would you like your taxi to be sent ?");
    labels.add(label1);

    // when we call upload csv with list of labels
    Response response = labelsResource.uploadCsv(callerMock, labels);

    // then it is successful
    assertThat(response, instanceOf(Response.class));
    assertEquals(200, response.getStatus());
    assertEquals("OK", response.getStatusInfo().toString());

    // then the list of labels is stored in the db
    List<Map<String, Object>> dbLabels = jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM labels")
        .mapToMap()
        .list());

    assertEquals(dbLabels.size(), 1); // should have one row
    Map<String, Object> dbLabel = dbLabels.get(0);
    assertEquals(dbLabel.size(), 3); // three columns
    assertEquals("OrderTaxi", dbLabel.get("label"));
    assertEquals("Order Taxi", dbLabel.get("shortdescription"));
    assertEquals("Where would you like your taxi to be sent ?", dbLabel.get("longdescription"));
  }

  @Test
  public void canReturnAllLabels()
  {
    // given this list of labels stored in the database
    List<Label> labels = new ArrayList<>();
    Label label1 = new Label();
    label1.setLabel("OrderTaxi");
    label1.setShortDescription("Order Taxi");
    label1.setLongDescription("Where would you like your taxi to be sent ?");
    labels.add(label1);
    labels.add(new Label("CancelTaxi", "Cancel Taxi", "Cancel my taxi"));
    labels.add(new Label("WhereTaxi", "Where taxi", "Where is my taxi"));
    Response response = labelsResource.uploadCsv(callerMock, labels);
    assertThat(response, instanceOf(Response.class));
    assertEquals(200, response.getStatus());

    // when we retrieve the list of labels

    response = labelsResource.getLabels(callerMock);

    // then we get backthe list of labels we expect
    // TODO use sql query here
    assertThat(response, instanceOf(Response.class));
    assertEquals(200, response.getStatus());
    assertEquals("OK", response.getStatusInfo().toString());
    assertEquals(((List) response.getEntity()).size(), 3);
  }

  @Test
  public void canAddASingleLabel()
  {
    Caller caller = new Caller("bob");
    Label label = new Label();
    label.setLabel("OrderTaxi");
    label.setShortDescription("Order Taxi");
    label.setLongDescription("Where would you like your taxi to be sent ?");

    Response response = labelsResource.upsert(caller, label);

    assertThat(response, instanceOf(Response.class));
    assertEquals(201, response.getStatus());

    // TODO check that record is in database
  }
}
