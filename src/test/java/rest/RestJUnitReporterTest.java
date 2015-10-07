package rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gherkin.formatter.Formatter;
import gherkin.formatter.model.*;
import java.util.ArrayList;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import cucumber.runtime.junit.JUnitReporter;

public class RestJUnitReporterTest {
   private RestJUnitReporter jiraJUnitReporter;
   private Formatter formatter = mock(Formatter.class);
   private RestRuntime runtime = mock(RestRuntime.class);
   private RestExecutionUnitRunner executionUnitRunner =
      mock(RestExecutionUnitRunner.class);
   private RunNotifier runNotifier = mock(RunNotifier.class);
   private JUnitReporter junitReporter = mock(JUnitReporter.class);
   private boolean strict = true;

   @SuppressWarnings("unchecked") private ArrayList<Step> listOfSteps =
      mock(ArrayList.class);
   private Step aStep = mock(Step.class);
   private Description description = mock(Description.class);
   private Match match = mock(Match.class);
   private Scenario scenario = mock(Scenario.class);

   @Test
   public void whenExecutionUnitRunnerAssigned() {
      jiraJUnitReporter =
         new RestJUnitReporter(junitReporter, formatter, strict, runtime);
      assertTrue(jiraJUnitReporter.getExecutionUnitRunner() == null);
      jiraJUnitReporter.startExecutionUnit(executionUnitRunner, runNotifier);
      assertTrue(jiraJUnitReporter.getExecutionUnitRunner() != null);
   }

   @Test
   public void whenResultIsLogged_thenFeatureAndResultMapPopulated() {
      jiraJUnitReporter =
         new RestJUnitReporter(junitReporter, formatter, strict, runtime);
      Result result = new Result(Result.PASSED, 011L, "DUMMY_ARG");
      jiraJUnitReporter.result(result);
      assertEquals(1, jiraJUnitReporter.getFeatureMap().size());
      assertEquals(1, jiraJUnitReporter.getResultMap().size());
   }

   @Test
   public void whenMatchIsCalled_thenCurrentStepIsAssigned() {
      String scenarioText = "When I cross the road(Scenario: Creating a mock2)";

      jiraJUnitReporter =
         new RestJUnitReporter(junitReporter, formatter, strict, runtime);

      jiraJUnitReporter.startExecutionUnit(executionUnitRunner, runNotifier);

      when(executionUnitRunner.getRunnerSteps()).thenReturn(listOfSteps);
      when(listOfSteps.get(0)).thenReturn(aStep);
      when(listOfSteps.remove(0)).thenReturn(aStep);
      when(executionUnitRunner.describeChild(aStep)).thenReturn(description);
      when(description.toString()).thenReturn(scenarioText);

      when(aStep.getName()).thenReturn("NAME");

      jiraJUnitReporter.startOfScenarioLifeCycle(scenario);
      jiraJUnitReporter.step(aStep);
      jiraJUnitReporter.match(match);

      String expected = removeBrackets(scenarioText);

      assertEquals("Current step has not been set correctly", expected,
         jiraJUnitReporter.getCurrentStep());
   }

   private String removeBrackets(String s) {
      char[] charArray = s.toCharArray();
      String sWithoutBrackets = "";
      for (char ch : charArray) {
         if (ch == '(' || ch == ')') {
            ch = ' ';
         }
         sWithoutBrackets += ch;
      }
      return sWithoutBrackets;
   }
}
