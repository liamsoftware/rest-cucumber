package rest;

import org.apache.commons.lang3.Validate;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.Resource;
import cucumber.runtime.io.ResourceLoader;

public class RestMultiLoader implements ResourceLoader {
   private MultiLoader loader;
   private CucumberRestClient restClient;

   public RestMultiLoader(ClassLoader classLoader) {
      loader = new MultiLoader(classLoader);
   }

   public void setRestClient(CucumberRestClient restClient) {
      Validate.notNull(restClient);
      this.restClient = restClient;
   }

   public boolean hasRestClientSet() {
      return restClient != null;
   }

   public Iterable<Resource> resources(String path, String suffix) {
      if (RestCucumberFeatureLoader.REST_CLIENT_KEY.equalsIgnoreCase(suffix)) {
         return new RestResourceLoader(restClient);
      }
      return loader.resources(path, suffix);
   }
}
