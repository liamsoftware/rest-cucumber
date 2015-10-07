package example;

import java.util.HashSet;
import java.util.Set;
import rest.CucumberRestClient;
import rest.ResultOutput;

public class MockRestClient implements CucumberRestClient {
   public static CucumberRestClient buildClient(String pathToProperties) {
      return new MockRestClient();
   }

   public MockRestClient() {
   }

   public String generateFeatureString(String issueKey) {
      issueKey = "test";
      return "Feature: Mock feature 1\n" + "Scenario: Creating a mock\n"
         + "Given I have 43 cukes in my belly\n" + "When I wait 1 hour\n"
         + "Then my belly should growl\n" + "Scenario: Creating a mock2\n"
         + "Given No oncoming traffic\n" + "When I cross the road\n"
         + "Then On the other side";
   }

   public boolean updateExecution(ResultOutput resultOutput) {
      return false;
   }

   public Set<String> getIssues() {
      Set<String> issues = new HashSet<String>();
      issues.add("1234");
      return issues;
   }
}
