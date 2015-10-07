package rest;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import cucumber.runtime.model.CucumberFeature;

public class SummariserTest {

   private Summariser summariser;
   private Map<String, String> results;
   private Map<String, CucumberFeature> features;
   private RestRuntime runtime;
   private CucumberFeature cucumberFeature;

   public void createSummariserWithValidMaps() {
      setUp();
      addValidMapEntries();
      createSummariser();
   }

   public void createSummariserWithInvalidMaps() {
      setUp();
      addValidMapEntries();
      addInvalidMapEntry();
      createSummariser();
   }

   public void setUp() {
      cucumberFeature = mock(CucumberFeature.class);
      when(cucumberFeature.getPath()).thenReturn("[TEST_PATH], [00:00:00]");
      results = new HashMap<String, String>();
      features = new HashMap<String, CucumberFeature>();
   }

   public void createSummariser() {
      runtime = mock(RestRuntime.class);
      summariser = new Summariser(results, features, runtime);
   }

   public void addValidMapEntries() {
      results.put("Then On the other side(Scenario: Creating a mock2) :", "passed");
      results.put("Given I have 43 cukes in my belly(Scenario: Creating a mock) :",
         "passed");
      results.put("When I wait 1 hour(Scenario: Creating a mock) :", "passed");
      features.put("Then On the other side(Scenario: Creating a mock2) :",
         cucumberFeature);
      features.put("Given I have 43 cukes in my belly(Scenario: Creating a mock) :",
         cucumberFeature);
      features.put("When I wait 1 hour(Scenario: Creating a mock) :", cucumberFeature);
   }

   public void addInvalidMapEntry() {
      results.put("This is an invalid step", "invalid result");
      features.put("This is also an invalid step", cucumberFeature);
   }

   @Test
   public void whenValidResultAreProvided_thenReturnListOfContainersIsPopulated() {
      createSummariserWithValidMaps();
      List<CucumberFeatureResultContainer> containers = summariser.getContainers();
      assertTrue(!containers.isEmpty());
   }

   @Test
   public void whenValidResultAreProvided_thenReturnListOfScenarioResultsIsPopulated() {
      createSummariserWithValidMaps();
      List<RestScenarioResult> scenarioResults = summariser.getScenarioResults();
      assertTrue(!scenarioResults.isEmpty());
   }
}
