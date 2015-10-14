package rest;

import static org.junit.Assert.assertNotNull;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class ResultOutputTest {
   private ResultOutput output;

   @Test
   public void givenValues_whenCtor_thenNoIssues() {
      String testId = "testId";
      String result = "passed";
      String comment = "comment";
      Map<String, String> scenarioResults = new HashMap<String, String>();
      scenarioResults.put("key1", "value1");
      scenarioResults.put("key2", "value2");
      output = new ResultOutput(testId, result, comment, scenarioResults);
      assertNotNull(output);
   }

}
