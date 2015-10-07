package rest;

import java.util.*;
import org.apache.commons.lang3.Validate;
import cucumber.runtime.io.Resource;

public class RestResourceLoader implements Iterable<Resource> {
   private List<RestResource> issues;
   private CucumberRestClient restClient;

   public RestResourceLoader(CucumberRestClient restClient) {
      Validate.notNull(restClient);
      this.restClient = restClient;
      issues = new ArrayList<RestResource>();
      loadIssues();
   }

   private void loadIssues() {
      Set<String> issueKeys = restClient.getIssues();
      Validate.notEmpty(issueKeys);
      for (String issueKey : issueKeys) {
         issues.add(new RestResource(issueKey, "LoadedFromRestClient", restClient));
      }
   }

   public List<RestResource> getIssues() {
      return issues;
   }

   public Iterator<Resource> iterator() {
      return new RestResourceIterator(issues);
   }
}
