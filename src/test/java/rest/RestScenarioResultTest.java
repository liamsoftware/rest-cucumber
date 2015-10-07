package rest;

import static org.junit.Assert.assertEquals;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class RestScenarioResultTest {

   private Map<String, String> stepsWithResults;
   private RestScenarioResult scenarioResult;

   @Test
   public void whenPassedStepsProvided_thenScenarioResultIsPass() {
      createPassSteps();
      scenarioResult = new RestScenarioResult("test_scenario", stepsWithResults);
      assertEquals("Scenario result should be \"pass\"", "pass",
         scenarioResult.getScenarioResult());
   }

   @Test
   public void whenFailedStepsProvided_thenScenarioResultIsFail() {
      createFailSteps();
      scenarioResult = new RestScenarioResult("test_scenario", stepsWithResults);
      assertEquals("Scenario result should be \"fail\"", "fail",
         scenarioResult.getScenarioResult());
   }

   @Test
   public void whenGettingTheScenarioName_theCorrectNameIsReturned() {
      createPassSteps();
      String aScenarioName = "aScenarioName";
      scenarioResult = new RestScenarioResult(aScenarioName, stepsWithResults);
      assertEquals("Scenario name is not set correctly", aScenarioName,
         scenarioResult.getScenario());
   }

   @Test
   public void whenGettingOverAllSummary_theDataReturnedIsValidAndCorrect() {
      createFailSteps();
      String aScenarioName = "aScenarioName";
      String expected = aScenarioName + ", fail";
      scenarioResult = new RestScenarioResult(aScenarioName, stepsWithResults);
      assertEquals("Scenario name is not set correctly", expected,
         scenarioResult.toString());
   }

   private void createPassSteps() {
      stepsWithResults = new HashMap<String, String>();
      stepsWithResults.put("step1", "[passed]");
      stepsWithResults.put("step2", "[passed]");
      stepsWithResults.put("step3", "[passed]");
      stepsWithResults.put("step4", "[passed]");
   }

   private void createFailSteps() {
      stepsWithResults = new HashMap<String, String>();
      stepsWithResults.put("step1", "[passed]");
      stepsWithResults.put("step2", "[failed]");
      stepsWithResults.put("step3", "[passed]");
      stepsWithResults.put("step4", "[failed]");
   }
}
