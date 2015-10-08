package rest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.apache.commons.lang3.Validate;
import cucumber.runtime.io.Resource;

public class RestResource implements Resource {
   private CucumberRestClient client;
   private String testId = null;
   private String path = null;
   private long creationTime;

   public RestResource(String testId, String baseUrl) {
      Validate.notEmpty(testId);
      Validate.notEmpty(baseUrl);
      this.testId = testId;
      path = baseUrl;
      creationTime = System.currentTimeMillis();
   }

   public RestResource(String testId, String baseUrl, CucumberRestClient restClient) {
      Validate.notEmpty(testId);
      Validate.notEmpty(baseUrl);
      Validate.notNull(restClient);
      this.testId = testId;
      path = baseUrl;
      client = restClient;
      creationTime = System.currentTimeMillis();
   }

   public String getTestId() {
      return testId;
   }

   public String getAbsolutePath() {
      return path == null ? "" : path;
   }

   public InputStream getInputStream() throws IOException {
      String result = client.generateFeatureString(testId);
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
      return Arrays.toString(getMetaDataArray());
   }

   private String[] getMetaDataArray() {
      String[] data = new String[2];
      data[0] = testId;
      data[1] = getTimeFormatted();
      return data;
   }

   private String getTimeFormatted() {
      long ss, mm, hh;
      ss = ((creationTime) / 1000) % 60;
      mm = ((creationTime) / (1000 * 60) % 60);
      hh = ((creationTime) / (1000 * 60 * 60) % 24);
      return String.format("%02d:%02d:%02d", hh, mm, ss);
   }
}
