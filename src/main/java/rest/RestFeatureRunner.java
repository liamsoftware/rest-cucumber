package rest;

import gherkin.formatter.model.Feature;
import java.util.ArrayList;
import java.util.List;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import cucumber.runtime.CucumberException;
import cucumber.runtime.model.*;

public class RestFeatureRunner extends ParentRunner<ParentRunner<?>> {
   private final List<ParentRunner<?>> children = new ArrayList<ParentRunner<?>>();
   private final CucumberFeature cucumberFeature;
   private final RestRuntime runtime;
   private final RestJUnitReporter jUnitReporter;

   public RestFeatureRunner(CucumberFeature cucumberFeature, RestRuntime runtime,
      RestJUnitReporter jUnitReporter) throws InitializationError {
      super(null);
      this.cucumberFeature = cucumberFeature;
      this.runtime = runtime;
      this.jUnitReporter = jUnitReporter;
      buildFeatureElementRunners();
   }

   @Override
   public String getName() {
      Feature feature = cucumberFeature.getGherkinFeature();
      return feature.getKeyword() + ": " + feature.getName();
   }

   @Override
   public List<ParentRunner<?>> getChildren() {
      return children;
   }

   @Override
   protected Description describeChild(ParentRunner<?> child) {
      return child.getDescription();
   }

   @Override
   protected void runChild(ParentRunner<?> child, RunNotifier notifier) {
      child.run(notifier);
   }
   
   @Override
   public void run(RunNotifier notifier) {
	   jUnitReporter.uri(cucumberFeature.getPath());
	   jUnitReporter.feature(cucumberFeature.getGherkinFeature());
	   super.run(notifier);
	   jUnitReporter.eof();
   }

   private void buildFeatureElementRunners() {
      for (CucumberTagStatement cucumberTagStatement : cucumberFeature
         .getFeatureElements()) {
         try {
            ParentRunner<?> featureElementRunner;
            if (cucumberTagStatement instanceof CucumberScenario) {
               featureElementRunner =
                  new RestExecutionUnitRunner(runtime, cucumberTagStatement,
                     jUnitReporter, cucumberFeature);
            } else {
               featureElementRunner =
                  new RestScenarioOutlineRunner(runtime,
                     (CucumberScenarioOutline) cucumberTagStatement, jUnitReporter,
                     cucumberFeature);
            }
            children.add(featureElementRunner);
         } catch (InitializationError e) {
            throw new CucumberException("Failed to create scenario runner", e);
         }
      }
   }
}
