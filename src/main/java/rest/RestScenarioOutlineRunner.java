package rest;

import java.util.ArrayList;
import java.util.List;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import cucumber.runtime.junit.JUnitReporter;
import cucumber.runtime.model.CucumberExamples;
import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.model.CucumberScenarioOutline;

public class RestScenarioOutlineRunner extends Suite {
   private final CucumberScenarioOutline cucumberScenarioOutline;
   private final JUnitReporter jUnitReporter;
   private Description description;
   private CucumberFeature cucumberFeature;
   private RestRuntime runtime;

   public RestScenarioOutlineRunner(RestRuntime runtime,
      CucumberScenarioOutline cucumberScenarioOutline, RestJUnitReporter jUnitReporter,
      CucumberFeature cucumberFeature) throws InitializationError {
      super(null, buildRunners(runtime, cucumberScenarioOutline, jUnitReporter,
         cucumberFeature));
      this.cucumberScenarioOutline = cucumberScenarioOutline;
      this.jUnitReporter = jUnitReporter;
      this.runtime = runtime;
      this.cucumberFeature = cucumberFeature;
   }

   private static List<Runner> buildRunners(RestRuntime runtime,
      CucumberScenarioOutline cucumberScenarioOutline, RestJUnitReporter jUnitReporter,
      CucumberFeature cucumberFeature) throws InitializationError {
      List<Runner> runners = new ArrayList<Runner>();
      for (CucumberExamples cucumberExamples : cucumberScenarioOutline
         .getCucumberExamplesList()) {
         runners.add(new RestExampleRunner(runtime, cucumberExamples, jUnitReporter,
            cucumberFeature));
      }
      return runners;
   }

   @Override
   public String getName() {
      return cucumberScenarioOutline.getVisualName();
   }

   @Override
   public Description getDescription() {
      if (description == null) {
         description =
            Description.createSuiteDescription(getName(),
               cucumberScenarioOutline.getGherkinModel());
         for (Runner child : getChildren()) {
            description.addChild(describeChild(child));
         }
      }
      return description;
   }

   @Override
   public void run(final RunNotifier notifier) {
      runtime.setCurrentCucumberFeature(cucumberFeature);
      cucumberScenarioOutline.formatOutlineScenario(jUnitReporter);
      super.run(notifier);
   }
}
