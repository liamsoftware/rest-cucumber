package example;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import rest.CucumberRestClient;
import rest.ResultOutput;

public class RestClientExample implements CucumberRestClient {
   public RestClientExample() {
   }

   public String getFeatureString(String testId) {
      return "Feature: Mock feature 1\n" + "Scenario: Creating a mock\n"
         + "Given I have 43 cukes in my belly\n" + "When I wait 1 hour\n"
         + "Then my belly should growl\n" + "Scenario: Creating a mock2\n"
         + "Given No oncoming traffic\n" + "When I cross the road\n"
         + "Then On the other side";
   }

   public boolean updateTest(ResultOutput resultOutput) {
      String testId = resultOutput.getTestId();
      String result = resultOutput.getResult();
      String stepResults = "\n";
      Map<String, String> results = resultOutput.getScenarioResults();
      for (String k : results.keySet()) {
         stepResults += k + " - " + results.get(k) + "\n";
      }
      String summary =
         String.format("TestID: %s, Result: %s, Step Results: %s", testId, result,
            stepResults);
      System.out.println(summary);
      return true;
   }

   public Set<String> getTestIds() {
      Set<String> testIds = new HashSet<String>();
      testIds.add("testId123");
      return testIds;
   }
}
