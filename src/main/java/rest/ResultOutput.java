package rest;

import java.util.Map;

public class ResultOutput {
   private static final String TEST_NAME = "Automated cucumber test";
   private String issueId;
   private String result;
   private String comment;
   private Map<String, String> scenarioResults;

   public ResultOutput(String issueId, String result, String comment,
      Map<String, String> scenarioResults) {
      this.issueId = issueId;
      this.result = result;
      this.comment = comment;
      this.scenarioResults = scenarioResults;
   }

   public String getIssueId() {
      return issueId;
   }

   public String getResult() {
      return result;
   }

   public String getComment() {
      return comment;
   }

   public Map<String, String> getScenarioResults() {
      return scenarioResults;
   }

   public String getTestName() {
      return TEST_NAME;
   }
}
