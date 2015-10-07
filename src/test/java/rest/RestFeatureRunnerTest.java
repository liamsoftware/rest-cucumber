package rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gherkin.formatter.model.Feature;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import cucumber.runtime.model.*;

public class RestFeatureRunnerTest {
   private RestFeatureRunner runner;

   private static final String TEST_KEYWORD = "testKeyword";
   private static final String TEST_NAME = "testName";
   private CucumberFeature cucumberFeature = mock(CucumberFeature.class);
   private RestRuntime runtime = mock(RestRuntime.class);
   private RestJUnitReporter reporter = mock(RestJUnitReporter.class);
   private Feature feature = mock(Feature.class);

   @Test
   public void whenGetName_thenNameIsReturned() throws InitializationError {
      List<CucumberTagStatement> featureElements = new ArrayList<CucumberTagStatement>();
      CucumberScenario scenario = mock(CucumberScenario.class);
      featureElements.add(scenario);
      CucumberScenarioOutline scenarioOutline = mock(CucumberScenarioOutline.class);
      featureElements.add(scenarioOutline);
      when(cucumberFeature.getFeatureElements()).thenReturn(featureElements);
      when(cucumberFeature.getGherkinFeature()).thenReturn(feature);
      when(feature.getKeyword()).thenReturn(TEST_KEYWORD);
      when(feature.getName()).thenReturn(TEST_NAME);
      runner = new RestFeatureRunner(cucumberFeature, runtime, reporter);
      assertEquals("Runner name not assigned correctly", runner.getName(), TEST_KEYWORD
         + ": " + TEST_NAME);
   }

   @Test
   public void whenGetChildren_thenListOfChildrenReturned() throws InitializationError {
      List<CucumberTagStatement> featureElements = new ArrayList<CucumberTagStatement>();
      CucumberScenario scenario = mock(CucumberScenario.class);
      featureElements.add(scenario);
      when(cucumberFeature.getFeatureElements()).thenReturn(featureElements);
      when(cucumberFeature.getGherkinFeature()).thenReturn(feature);
      when(feature.getKeyword()).thenReturn(TEST_KEYWORD);
      when(feature.getName()).thenReturn(TEST_NAME);
      runner = new RestFeatureRunner(cucumberFeature, runtime, reporter);
      List<ParentRunner<?>> listOfChildren = runner.getChildren();
      assertTrue(!listOfChildren.isEmpty());
   }
}
