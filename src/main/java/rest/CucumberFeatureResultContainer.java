package rest;

import java.util.*;
import cucumber.runtime.model.CucumberFeature;

public class CucumberFeatureResultContainer {
   private CucumberFeature cucumberFeature;
   private List<RestScenarioResult> results;
   private String testId;

   public CucumberFeatureResultContainer(CucumberFeature cucumberFeature) {
      this.cucumberFeature = cucumberFeature;
      this.results = new ArrayList<RestScenarioResult>();
      setTestId();
   }

   public Map<String, String> getStepResults() {
      Map<String, String> stepResults = new HashMap<String, String>();
      for (RestScenarioResult aRestScenarioResult : results) {
         Map<String, String> stepResult = aRestScenarioResult.getStepsWithResults();
         for (String step : stepResult.keySet()) {
            stepResults.put(step, stepResult.get(step));
         }
      }
      stepResults = sanitiseResults(stepResults);
      return stepResults;
   }

   private Map<String, String> sanitiseResults(Map<String, String> stepResults) {
      Map<String, String> sanitisedScenarioResults = new HashMap<String, String>();
      for (String k : stepResults.keySet()) {
         String result = stepResults.get(k);
         String step = new String(k);
         step = step.replace("[", "");
         step = step.replace("]", "");
         step = step.substring(1).trim();
         result = result.replace("[", "");
         result = result.replace("]", "");
         sanitisedScenarioResults.put(step, result);
      }
      return sanitisedScenarioResults;
   }

   public String getComment() {
      String comment = "";
      for (RestScenarioResult r : results) {
         comment += "[" + r.getScenarioResult() + "] " + r.getScenario() + "\n";
      }
      return comment;
   }

   public String getTestId() {
      return testId;
   }

   public boolean addResult(RestScenarioResult restScenarioResult) {
      return results.add(restScenarioResult);
   }

   public String toString() {
      return testId + " [No. Scenarios: " + results.size() + "]" + featureTestResult();
   }

   public String featureTestResult() {
      if (results.isEmpty()) {
         return "No scenario results have been logged for issue " + getTestId();
      }

      String result = "Passed";
      int passCount = 0;
      for (RestScenarioResult aRestScenarioResult : results) {
         if (!"pass".equals(aRestScenarioResult.getScenarioResult())) {
            result = "Failed";
         } else {
            passCount++;
         }
      }
      return testId + "[Feature Result:" + result + "] [" + passCount + " out of "
         + results.size() + " passed]";
   }

   public String getFeatureResult() {
      String result = "Passed";
      for (RestScenarioResult aRestScenarioResult : results) {
         if (!"pass".equals(aRestScenarioResult.getScenarioResult())) {
            result = "Failed";
         }
      }
      return result;
   }

   private void setTestId() {
      String[] meta = cucumberFeature.getPath().split(",");
      testId = meta[0];

      if (testId.contains("[")) {
         testId = testId.replace("[", "");
      }
      if (testId.contains("]")) {
         testId = testId.replace("]", "");
      }
   }
}