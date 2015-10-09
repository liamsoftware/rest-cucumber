package rest;

import java.util.Set;

public interface CucumberRestClient {
   boolean updateTest(ResultOutput resultOutput);

   Set<String> getTestIds();

   String getFeatureString(String testId);
}
