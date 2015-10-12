package rest;

import gherkin.formatter.model.Step;
import org.apache.commons.lang3.Validate;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import cucumber.runtime.junit.ExecutionUnitRunner;
import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.model.CucumberScenario;
import cucumber.runtime.model.CucumberTagStatement;

public class RestExecutionUnitRunner extends ExecutionUnitRunner {
   private final RestRuntime runtime;
   private final CucumberFeature cucumberFeature;

   public RestExecutionUnitRunner(RestRuntime runtime,
      CucumberTagStatement cucumberScenario, RestJUnitReporter jUnitReporter,
      CucumberFeature cucumberFeature) throws InitializationError {
      super(runtime.getRuntime(), (CucumberScenario) cucumberScenario, jUnitReporter);
      this.runtime = Validate.notNull(runtime);
      this.cucumberFeature = Validate.notNull(cucumberFeature);
   }

   @Override
   public void run(final RunNotifier notifier) {
      runtime.setCurrentCucumberFeature(cucumberFeature);
      super.run(notifier);
   }

   @Override
   public Description describeChild(Step step) {
      return super.describeChild(step);
   }
}
