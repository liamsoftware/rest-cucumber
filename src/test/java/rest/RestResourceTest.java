package rest;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;
import example.MockRestClient;

public class RestResourceTest {
   private MockRestClient client = mock(MockRestClient.class);
   private RestResource jiraResource;
   private String dummyFeatureString = "Feature: example feature \n"
      + "Scenario: Credit card number too short \n"
      + "Given I have chosen to buy items \n"
      + "When I enter a card number that is only 10 digits long \n"
      + "Then the form should be redisplayed";

   @Test
   public void whenRestClientProvided_thenFeatureStringIsCreatedCorrectly()
      throws IOException {
      when(client.generateFeatureString(anyString())).thenReturn(dummyFeatureString);
      jiraResource = new RestResource("TEST-1234", "www.base.url", client);

      InputStream inputStream = jiraResource.getInputStream();
      assertTrue(inputStream != null);
   }

   @Test(expected = IOException.class)
   public void whenRestClientWithEmptyFeatureStringIsProvided_thenAnExceptionIsThrown()
      throws IOException {
      when(client.generateFeatureString(anyString())).thenReturn("");
      jiraResource = new RestResource("TEST-1234", "www.base.url", client);
      InputStream inputStream = jiraResource.getInputStream();
      assertTrue(inputStream != null);
   }

   @Test(expected = IllegalArgumentException.class)
   public void whenEmptyIssueKeyProvided_anErrorIsThrown() {
      jiraResource = new RestResource("", "www.base.url");
   }

   @Test(expected = IllegalArgumentException.class)
   public void whenEmptyBaseUrlProvided_anErrorIsThrown() {
      jiraResource = new RestResource("TEST-123", "");
   }

   @Test(expected = NullPointerException.class)
   public void whenNullClientProvided_anErrorIsThrown() {
      jiraResource = new RestResource("TEST-123", "www.base.url", null);
   }
}
