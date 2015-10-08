package rest;

import java.util.*;
import org.apache.commons.lang3.Validate;
import cucumber.runtime.io.Resource;

public class RestResourceLoader implements Iterable<Resource> {
   private List<RestResource> testCases;
   private CucumberRestClient restClient;

   public RestResourceLoader(CucumberRestClient restClient) {
      Validate.notNull(restClient);
      this.restClient = restClient;
      testCases = new ArrayList<RestResource>();
      loadTestCases();
   }

   private void loadTestCases() {
      Set<String> testIds = restClient.getTestIds();
      Validate.notEmpty(testIds);
      for (String testId : testIds) {
         testCases.add(new RestResource(testId, "LoadedFromRestClient", restClient));
      }
   }

   public List<RestResource> getTestCases() {
      return testCases;
   }

   public Iterator<Resource> iterator() {
      return new RestResourceIterator(testCases);
   }
}
