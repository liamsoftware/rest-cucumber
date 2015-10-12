package rest;

import java.util.*;

public class RestScenarioResult {
   public final String scenario;
   private static final String PASSED_STEP_RESULT = "[passed]";
   private static final String PASS_SCENARIO_RESULT = "pass";
   private static final String FAIL_SCENARIO_RESULT = "fail";
   private Map<String, String> stepsWithResults;
   private List<String> passedSteps;
   private List<String> failedSteps;
   private String scenarioResult;

   public RestScenarioResult(String scenario, Map<String, String> stepsWithResults) {
      this.scenario = scenario;
      this.stepsWithResults = stepsWithResults;
      passedSteps = new ArrayList<String>();
      failedSteps = new ArrayList<String>();
      calculateResult();
   }

   private void calculateResult() {
      for (String step : stepsWithResults.keySet()) {
         String stepResult = stepsWithResults.get(step);
         if (PASSED_STEP_RESULT.equalsIgnoreCase(stepResult)) {
            passedSteps.add(step);
         } else {
            failedSteps.add(step);
         }
      }
      scenarioResult =
         failedSteps.isEmpty() ? PASS_SCENARIO_RESULT : FAIL_SCENARIO_RESULT;
      if (failedSteps.isEmpty() && passedSteps.isEmpty()) {
         scenarioResult = FAIL_SCENARIO_RESULT;
      }
   }

   public String getScenarioResult() {
      return scenarioResult;
   }

   public Map<String, String> getStepsWithResults() {
      return Collections.unmodifiableMap(stepsWithResults);
   }

   public String toString() {
      return scenario + ", " + scenarioResult;
   }
}
