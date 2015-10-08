package rest;

import java.util.Set;

public interface CucumberRestClient {
   String generateFeatureString(String testId);

   boolean updateTest(ResultOutput resultOutput);

   Set<String> getTestIds();
}
