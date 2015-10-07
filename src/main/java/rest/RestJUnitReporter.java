package rest;

import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Match;
import gherkin.formatter.model.Result;
import gherkin.formatter.model.Step;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import cucumber.runtime.junit.ExecutionUnitRunner;
import cucumber.runtime.junit.JUnitReporter;
import cucumber.runtime.model.CucumberFeature;

public class RestJUnitReporter extends JUnitReporter {
   private String currentStep;
   private RestExecutionUnitRunner executionUnitRunner;
   private Map<String, String> results;
   private Map<String, CucumberFeature> features;
   private Summariser summariser;
   private RestRuntime runtime;
   private CucumberFeature currentCucumberFeature;

   public RestJUnitReporter(Reporter reporter, Formatter formatter, boolean strict,
      RestRuntime runtime) {
      super(reporter, formatter, strict);
      this.runtime = runtime;
      results = new HashMap<String, String>();
      features = new HashMap<String, CucumberFeature>();
   }

   @Override
   public void result(Result result) {
      currentCucumberFeature = runtime.getCurrentCucumberFeature();
      features.put(currentStep, currentCucumberFeature);
      results.put(currentStep, result.getStatus());
      super.result(result);
   }

   @Override
   public void startExecutionUnit(ExecutionUnitRunner executionUnitRunner,
      RunNotifier runNotifier) {
      if (executionUnitRunner instanceof RestExecutionUnitRunner) {
         this.executionUnitRunner = (RestExecutionUnitRunner) executionUnitRunner;
      }
      super.startExecutionUnit(executionUnitRunner, runNotifier);
   }

   @Override
   public void match(Match match) {
      assignCurrentStep();
      super.match(match);
   }

   private void assignCurrentStep() {
      List<Step> steps = executionUnitRunner.getRunnerSteps();
      Step runnerStep = steps.get(0);
      Description description = executionUnitRunner.describeChild(runnerStep);
      currentStep = description.toString();
      currentStep = currentStep == null ? null : removeBrackets(currentStep);
   }

   private String removeBrackets(String s) {
      char[] charArray = s.toCharArray();
      String sWithoutBrackets = "";
      for (char ch : charArray) {
         if (ch == '(' || ch == ')') {
            ch = ' ';
         }
         sWithoutBrackets += ch;
      }
      return sWithoutBrackets;
   }

   @Override
   public void close() {
      super.close();
   }

   public void closeWithSummariser() {
      summariser = new Summariser(results, features, runtime);
      close();
   }

   public RestExecutionUnitRunner getExecutionUnitRunner() {
      return executionUnitRunner;
   }

   public List<RestScenarioResult> getJiraScenarioResults() {
      return summariser.getScenarioResults();
   }

   public Map<String, String> getResultMap() {
      return results;
   }

   public Map<String, CucumberFeature> getFeatureMap() {
      return features;
   }

   public String getCurrentStep() {
      return currentStep;
   }
}