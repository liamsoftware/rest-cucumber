package rest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import org.junit.Test;
import cucumber.runtime.Runtime;
import cucumber.runtime.model.CucumberFeature;

public class RestRuntimeTest {

   private CucumberFeature cucumberFeature = mock(CucumberFeature.class);
   private CucumberFeatureResultContainer container =
      mock(CucumberFeatureResultContainer.class);
   private Runtime runtime = mock(Runtime.class);
   private RestRuntime jiraRuntime;

   @Test
   public void whenCucumberFeatureIsSet_thenTheCorrectFeatureIsReturned() {
      jiraRuntime = new RestRuntime(runtime);
      jiraRuntime.setCurrentCucumberFeature(cucumberFeature);
      CucumberFeature fromRuntime = jiraRuntime.getCurrentCucumberFeature();
      assertEquals("Cucumber Feature not set incorrectly", cucumberFeature, fromRuntime);
   }

   @Test
   public void whenResultContainerIsSet_thenTheCorrectContainerResultIsReturned() {
      jiraRuntime = new RestRuntime(runtime);
      jiraRuntime.addContainer(container);
      CucumberFeatureResultContainer fromRuntime = jiraRuntime.getResults().get(0);
      assertEquals("Cucumber Feature Result Container set incorrectly", container,
         fromRuntime);
   }

   @Test
   public void whenGetRuntime_thenRuntimeIsReturned() {
      jiraRuntime = new RestRuntime(runtime);
      Runtime fromRuntime = jiraRuntime.getRuntime();
      assertEquals("Runtime set incorrectly", runtime, fromRuntime);
   }

}