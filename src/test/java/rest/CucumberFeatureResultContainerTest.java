package rest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.Test;
import cucumber.runtime.model.CucumberFeature;

public class CucumberFeatureResultContainerTest {

   private CucumberFeature cucumberFeature = mock(CucumberFeature.class);
   private RestScenarioResult result;
   private CucumberFeatureResultContainer container;

   @Test
   public void whenContainerCreated_thenIssueKeyAssignedCorrectly() {
      when(cucumberFeature.getPath()).thenReturn("[TEST-123],[00:00:00]");
      container = new CucumberFeatureResultContainer(cucumberFeature);
      assertEquals("IssueKey has not been set correctly", container.getTestId(),
         "TEST-123");
   }

   @Test
   public void whenPassScenarioResultsAdded_thenContainerResultIsPass() {
      when(cucumberFeature.getPath()).thenReturn("[TEST-123],[00:00:00]");
      container = new CucumberFeatureResultContainer(cucumberFeature);
      result = mock(RestScenarioResult.class);
      when(result.getScenarioResult()).thenReturn("pass");
      container.addResult(result);
      container.addResult(result);
      container.addResult(result);
      assertEquals("Container result should be \"pass\"", container.featureTestResult(),
         "TEST-123[Feature Result:Passed] [3 out of 3 passed]");
   }

   @Test
   public void whenPassAndFailScenarioResultsAdded_thenContainerResultIsFail() {
      when(cucumberFeature.getPath()).thenReturn("[TEST-123],[00:00:00]");
      container = new CucumberFeatureResultContainer(cucumberFeature);
      result = mock(RestScenarioResult.class);
      when(result.getScenarioResult()).thenReturn("pass").thenReturn("fail")
         .thenReturn("pass");
      container.addResult(result);
      container.addResult(result);
      container.addResult(result);
      assertEquals("Container result should be \"fail\"", container.featureTestResult(),
         "TEST-123[Feature Result:Failed] [2 out of 3 passed]");
   }

   @Test
   public void whenFailScenarioResultsAdded_thenContainerResultIsFail() {
      when(cucumberFeature.getPath()).thenReturn("[TEST-123],[00:00:00]");
      container = new CucumberFeatureResultContainer(cucumberFeature);
      result = mock(RestScenarioResult.class);
      when(result.getScenarioResult()).thenReturn("fail");
      container.addResult(result);
      container.addResult(result);
      container.addResult(result);
      assertEquals("Container result should be \"fail\"", container.featureTestResult(),
         "TEST-123[Feature Result:Failed] [0 out of 3 passed]");
   }

   @Test
   public void whenZeroScenarioResultsAdded_thenNoResultsMessageIsDisplayed() {
      when(cucumberFeature.getPath()).thenReturn("[TEST-123],[00:00:00]");
      container = new CucumberFeatureResultContainer(cucumberFeature);
      assertEquals("There should be no scenario results", container.featureTestResult(),
         "No scenario results have been logged for issue TEST-123");
   }

   @Test
   public void
      givenValidFeatureResultContainer_whenGetStepResultsCalled_thenStepResultsReturned() {

   }
}
