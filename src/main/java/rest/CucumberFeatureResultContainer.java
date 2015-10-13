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
         stepResults.putAll(aRestScenarioResult.getStepsWithResults());
      }
      stepResults = sanitiseResults(stepResults);
      return stepResults;
   }

   private Map<String, String> sanitiseResults(Map<String, String> stepResults) {
      Map<String, String> sanitisedScenarioResults = new HashMap<String, String>();
      for (Map.Entry<String, String> entry : stepResults.entrySet()) {
         String entryValue = entry.getValue();
         String entryKey = entry.getKey();
         entryKey = entryKey.replace("[", "");
         entryKey = entryKey.replace("]", "");
         entryKey = entryKey.substring(1).trim();
         entryValue = entryValue.replace("[", "");
         entryValue = entryValue.replace("]", "");
         sanitisedScenarioResults.put(entryKey, entryValue);
      }
      return sanitisedScenarioResults;
   }

   public String getComment() {
      StringBuilder comment = new StringBuilder();
      for (RestScenarioResult r : results) {
         comment.append("[").append(r.getScenarioResult()).append("] ")
            .append(r.scenario).append("\n");
      }
      return comment.toString();
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