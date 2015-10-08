package rest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import cucumber.runtime.io.Resource;

public class RestMultiLoaderTest {

   private ClassLoader classLoader = mock(ClassLoader.class);
   private CucumberRestClient restClient = mock(CucumberRestClient.class);
   private RestMultiLoader jiraMultiLoader;

   @Before
   public void setUp() {
      Set<String> issues = new HashSet<String>();
      issues.add("2322");
      when(restClient.getTestIds()).thenReturn(issues);
   }

   @Test
   public void whenJiraResourceRequested_thenJiraResourceLoaderReturned() {
      jiraMultiLoader = new RestMultiLoader(classLoader);
      jiraMultiLoader.setRestClient(restClient);
      Iterable<Resource> resource =
         jiraMultiLoader.resources("", RestCucumberFeatureLoader.REST_CLIENT_KEY);
      assertTrue(resource.iterator().hasNext());
   }

   @Test(expected = IllegalArgumentException.class)
   public void whenInvalidResourceRequested_thenAnErrorIsThrown() {
      jiraMultiLoader = new RestMultiLoader(classLoader);
      Iterable<Resource> resource = jiraMultiLoader.resources("", "");
      assertFalse(resource.iterator().hasNext());
   }
}
