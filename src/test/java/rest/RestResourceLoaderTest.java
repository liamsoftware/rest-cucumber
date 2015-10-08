package rest;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import org.junit.Before;
import org.junit.Test;
import cucumber.runtime.io.Resource;
import example.MockRestClient;

public class RestResourceLoaderTest {
   private CucumberRestClient restClient = mock(MockRestClient.class);

   @Before
   public void setUp() {
      Set<String> issues = new HashSet<String>();
      issues.add("5555");
      when(restClient.getTestIds()).thenReturn(issues);
   }

   @Test
   public void whenPropertiesFileComplete_thenIssuesListIsPopulated()
      throws FileNotFoundException, IOException {
      RestResourceLoader loader = new RestResourceLoader(restClient);
      List<RestResource> resourceList = loader.getTestCases();
      assertTrue(!resourceList.isEmpty());
   }

   @Test
   public void givenValidJiraPropertiesFile_iteratorCreatedSuccessfully()
      throws FileNotFoundException, IOException {
      RestResourceLoader loader = new RestResourceLoader(restClient);
      Iterator<Resource> it = loader.iterator();
      assertTrue(it.hasNext());
   }
}
