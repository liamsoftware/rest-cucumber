package rest;

import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Match;
import gherkin.formatter.model.Result;
import gherkin.formatter.model.Step;
import java.util.*;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import cucumber.runtime.junit.ExecutionUnitRunner;
import cucumber.runtime.junit.JUnitReporter;
import cucumber.runtime.model.CucumberFeature;

public class RestJUnitReporter extends JUnitReporter {
   private String currentStep;
   private RestExecutionUnitRunner executionUnitRunner;
   private Summariser summariser;
   private final RestRuntime runtime;
   private Map<String, String> results;
   private Map<String, CucumberFeature> features;

   public RestJUnitReporter(Reporter reporter, Formatter formatter, boolean strict,
      RestRuntime runtime) {
      super(reporter, formatter, strict);
      this.runtime = runtime;
      results = new HashMap<String, String>();
      features = new HashMap<String, CucumberFeature>();
   }

   @Override
   public void result(Result result) {
      CucumberFeature currentCucumberFeature = runtime.getCurrentCucumberFeature();
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
      if (currentStep.contains("(") || currentStep.contains(")")) {
         currentStep = removeBrackets(currentStep);
      }
   }

   private String removeBrackets(String s) {
      char[] charArray = s.toCharArray();
      StringBuilder sWithoutBrackets = new StringBuilder();
      for (char ch : charArray) {
         if (ch == '(' || ch == ')') {
            ch = ' ';
         }
         sWithoutBrackets.append(ch);
      }
      return sWithoutBrackets.toString();
   }

   public void closeWithSummariser() {
      summariser = new Summariser(results, features, runtime);
      close();
   }

   public RestExecutionUnitRunner getExecutionUnitRunner() {
      return executionUnitRunner;
   }

   public List<RestScenarioResult> getRestScenarioResults() {
      return summariser.getScenarioResults();
   }

   public Map<String, String> getResultMap() {
      return Collections.unmodifiableMap(results);
   }

   public Map<String, CucumberFeature> getFeatureMap() {
      return Collections.unmodifiableMap(features);
   }

   public String getCurrentStep() {
      return currentStep;
   }
}