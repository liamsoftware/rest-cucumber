package rest;

import java.util.ArrayList;
import java.util.List;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import cucumber.runtime.junit.ExamplesRunner;
import cucumber.runtime.junit.ExecutionUnitRunner;
import cucumber.runtime.junit.JUnitReporter;
import cucumber.runtime.model.CucumberExamples;
import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.model.CucumberScenario;

public class RestExampleRunner extends Suite {
   private final CucumberExamples cucumberExamples;
   private Description description;
   private JUnitReporter jUnitReporter;

   protected RestExampleRunner(RestRuntime runtime, CucumberExamples cucumberExamples,
      RestJUnitReporter jUnitReporter, CucumberFeature cucumberFeature)
      throws InitializationError {
      super(ExamplesRunner.class, buildRunners(runtime, cucumberExamples, jUnitReporter,
         cucumberFeature));
      this.cucumberExamples = cucumberExamples;
      this.jUnitReporter = jUnitReporter;
   }

   private static List<Runner> buildRunners(RestRuntime runtime,
      CucumberExamples cucumberExamples, RestJUnitReporter jUnitReporter,
      CucumberFeature cucumberFeature) {
      List<Runner> runners = new ArrayList<Runner>();
      List<CucumberScenario> exampleScenarios = cucumberExamples.createExampleScenarios();
      for (CucumberScenario scenario : exampleScenarios) {
         try {
            ExecutionUnitRunner exampleScenarioRunner =
               new RestExecutionUnitRunner(runtime, scenario, jUnitReporter,
                  cucumberFeature);
            runners.add(exampleScenarioRunner);
         } catch (InitializationError initializationError) {
            initializationError.printStackTrace();
         }
      }
      return runners;
   }

   @Override
   protected String getName() {
      return cucumberExamples.getExamples().getKeyword() + ": "
         + cucumberExamples.getExamples().getName();
   }

   @Override
   public Description getDescription() {
      if (description == null) {
         description =
            Description.createSuiteDescription(getName(), cucumberExamples.getExamples());
         for (Runner child : getChildren()) {
            description.addChild(describeChild(child));
         }
      }
      return description;
   }

   @Override
   public void run(final RunNotifier notifier) {
      jUnitReporter.examples(cucumberExamples.getExamples());
      super.run(notifier);
   }
}
