package rest;

import java.util.*;
import cucumber.runtime.model.CucumberFeature;

public class CucumberFeatureResultContainer {
   private CucumberFeature cucumberFeature;
   private List<RestScenarioResult> results;
   private String issueKey;

   public CucumberFeatureResultContainer(CucumberFeature cucumberFeature) {
      this.cucumberFeature = cucumberFeature;
      this.results = new ArrayList<RestScenarioResult>();
      setIssueKey();
   }

   public Map<String, String> getStepResults() {
      Map<String, String> stepResults = new HashMap<String, String>();
      for (RestScenarioResult jsr : results) {
         Map<String, String> sResult = jsr.getStepsWithResults();
         for (String key : sResult.keySet()) {
            stepResults.put(key, sResult.get(key));
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

   public String getIssueKey() {
      return issueKey;
   }

   public boolean addResult(RestScenarioResult jiraScenarioResult) {
      return results.add(jiraScenarioResult);
   }

   public String toString() {
      return issueKey + " [No. Scenarios: " + results.size() + "]" + featureTestResult();
   }

   public String featureTestResult() {
      if (results.isEmpty()) {
         return "No scenario results have been logged for issue " + getIssueKey();
      }

      String result = "Passed";
      int passCount = 0;
      for (RestScenarioResult jsr : results) {
         if (!"pass".equals(jsr.getScenarioResult())) {
            result = "Failed";
         } else {
            passCount++;
         }
      }
      return issueKey + "[Feature Result:" + result + "] [" + passCount + " out of "
         + results.size() + " passed]";
   }

   public String getFeatureResult() {
      String result = "Passed";
      for (RestScenarioResult jsr : results) {
         if (!"pass".equals(jsr.getScenarioResult())) {
            result = "Failed";
         }
      }
      return result;
   }

   private void setIssueKey() {
      String[] meta = cucumberFeature.getPath().split(",");
      issueKey = meta[0];

      if (issueKey.contains("[")) {
         issueKey = issueKey.replace("[", "");
      }
      if (issueKey.contains("]")) {
         issueKey = issueKey.replace("]", "");
      }
   }
}