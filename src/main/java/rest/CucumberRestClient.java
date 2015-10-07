package rest;

import java.util.Set;

public interface CucumberRestClient {
   String generateFeatureString(String issueKey);

   boolean updateExecution(ResultOutput resultOutput);

   Set<String> getIssues();
}
