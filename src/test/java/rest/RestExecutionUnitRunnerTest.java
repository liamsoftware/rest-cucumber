package rest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import org.junit.Test;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import cucumber.runtime.Runtime;
import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.model.CucumberScenario;

public class RestExecutionUnitRunnerTest {
   private RestRuntime runtime = mock(RestRuntime.class);
   private CucumberScenario statement = mock(CucumberScenario.class);
   private RestJUnitReporter reporter = mock(RestJUnitReporter.class);
   private CucumberFeature feature = mock(CucumberFeature.class);

   @Test
   public void whenValidParamPassed_runnerIsCreatedSuccesfully()
      throws InitializationError {
      new RestExecutionUnitRunner(runtime, statement, reporter, feature);
   }

   @Test(expected = NullPointerException.class)
   public void whenNullRuntimeParamPassed_anExceptionIsThrown()
      throws InitializationError {
      new RestExecutionUnitRunner(null, statement, reporter, feature);
   }

   @Test(expected = NullPointerException.class)
   public void whenNullFeatureParamPassed_anExceptionIsThrown()
      throws InitializationError {
      new RestExecutionUnitRunner(runtime, statement, reporter, null);
   }

   @Test
   public void whenRunIsCalled_callIsMadeToSuper() throws InitializationError {
      RestRuntime jRuntime = new RestRuntime(mock(Runtime.class));
      RestExecutionUnitRunner runner =
         new RestExecutionUnitRunner(jRuntime, statement, reporter, feature);
      RunNotifier notifier = mock(RunNotifier.class);
      runner.run(notifier);
      CucumberFeature featureAtRuntime = jRuntime.getCurrentCucumberFeature();
      assertEquals("Cucumber Feature incorrectly set", feature, featureAtRuntime);
   }
}
