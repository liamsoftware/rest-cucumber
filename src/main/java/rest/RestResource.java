package rest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.apache.commons.lang3.Validate;
import cucumber.runtime.io.Resource;

public class RestResource implements Resource {
   private CucumberRestClient client;
   public final String testId;
   public final String path;

   public RestResource(String testId, String baseUrl) {
      this.testId = Validate.notEmpty(testId);
      path = Validate.notEmpty(baseUrl);
   }

   public RestResource(String testId, String baseUrl, CucumberRestClient restClient) {
      this.testId = Validate.notEmpty(testId);
      path = Validate.notEmpty(baseUrl);
      client = Validate.notNull(restClient);
   }

   public String getAbsolutePath() {
      return path == null ? "" : path;
   }

   public InputStream getInputStream() throws IOException {
      String result = client.getFeatureString(testId);
      if (result.isEmpty()) {
         throw new IOException("The feature string recieved from Rest Client is empty.");
      }
      return new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8));
   }

   public String getClassName(String extension) {
      // Not used, but needed for the Resource Interface
      return extension + " - is not assigned";
   }

   public String getPath() {
      return path;
   }
}
