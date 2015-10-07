package rest;

import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import cucumber.runtime.model.CucumberFeature;

public class RestCucumberFeatureLoaderTest {
   private String feature =
      "Feature: this is a feature Scenario a feature scenario Given something When something else Then return true";

   @Test
   public void
      givenValidIssueSet_whenLoadingFromJira_thenListOfCucumberFeaturesIsCreated()
         throws IOException {
      List<String> featurePaths = new ArrayList<String>();
      featurePaths.add("jira");
      RestResourceLoader resourceLoader = mock(RestResourceLoader.class);

      RestMultiLoader multiLoader = mock(RestMultiLoader.class);
      given(multiLoader.resources("", "jira")).willReturn(resourceLoader);
      RestResource jr = mock(RestResource.class);
      RestResourceIterator jit = mock(RestResourceIterator.class);
      given(resourceLoader.iterator()).willReturn(jit);
      given(jit.hasNext()).willReturn(true).willReturn(false);
      given(jit.next()).willReturn(jr);
      given(jr.getInputStream()).willReturn(
         new ByteArrayInputStream(feature.getBytes(StandardCharsets.UTF_8)));
      given(jr.getPath()).willReturn("thisIsAPath");

      List<CucumberFeature> cucumberFeatures =
         RestCucumberFeatureLoader.load(multiLoader, featurePaths,
            new ArrayList<Object>(), System.out);
      assertTrue("Cucumber Feature should not be null from Jira Resource",
         cucumberFeatures != null);
   }

   @Test
   public void
      givenValidFeatureSet_whenLoadingFromFile_thenListOfCucumberFeaturesIsCreated() {
      List<String> featurePaths = new ArrayList<String>();
      featurePaths.add("src/main/java/example");
      ClassLoader classLoader = mock(ClassLoader.class);
      RestMultiLoader multiLoader = new RestMultiLoader(classLoader);
      List<CucumberFeature> cucumberFeatures =
         RestCucumberFeatureLoader.load(multiLoader, featurePaths,
            new ArrayList<Object>(), System.out);
      assertTrue("Cucumber Feature should not be null from File Resource",
         cucumberFeatures != null);
   }
}
