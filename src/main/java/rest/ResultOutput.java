package rest;

import java.util.Collections;
import java.util.Map;

public class ResultOutput {
   public static final String testName = "Automated cucumber test";
   public final String testId;
   public final String result;
   public final String comment;
   public final Map<String, String> scenarioResults;

   public ResultOutput(String testId, String result, String comment,
      Map<String, String> scenarioResults) {
      this.testId = testId;
      this.result = result;
      this.comment = comment;
      this.scenarioResults = Collections.unmodifiableMap(scenarioResults);
   }
   
   public String getTestId() {
	   return testId;
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
	   return testName;
   }
}
