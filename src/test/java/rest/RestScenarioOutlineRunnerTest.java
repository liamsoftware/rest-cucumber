package rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import gherkin.formatter.model.Examples;
import gherkin.formatter.model.TagStatement;
import java.util.ArrayList;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.InitializationError;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import cucumber.runtime.model.CucumberExamples;
import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.model.CucumberScenarioOutline;

public class RestScenarioOutlineRunnerTest {

   private static final String MOCK_TEST_NAME = "MockTest";
   @Mock private RestRuntime restRuntime;
   @Mock private CucumberScenarioOutline cucumberScenarioOutline;
   @Mock private RestJUnitReporter restJunitReporter;
   @Mock private CucumberFeature cucumberFeature;
   @Mock private CucumberExamples cucumberExamples;
   @Mock private TagStatement tagStatement;
   @Mock private Examples examples;
   private ArrayList<CucumberExamples> cucumberExamplesList =
      new ArrayList<CucumberExamples>();
   private RestScenarioOutlineRunner restScenarioOutlineRunner;

   private void givenValidRestScenarioOutlineRunner() {
      MockitoAnnotations.initMocks(this);
      cucumberExamplesList.add(cucumberExamples);
      given(cucumberScenarioOutline.getVisualName()).willReturn(MOCK_TEST_NAME);
      given(cucumberScenarioOutline.getCucumberExamplesList()).willReturn(
         cucumberExamplesList);
      given(cucumberScenarioOutline.getGherkinModel()).willReturn(tagStatement);
      given(cucumberExamples.getExamples()).willReturn(examples);
      given(examples.getName()).willReturn("Given");
      given(examples.getKeyword()).willReturn("Given");
      try {
         restScenarioOutlineRunner =
            new RestScenarioOutlineRunner(restRuntime, cucumberScenarioOutline,
               restJunitReporter, cucumberFeature);
      } catch (InitializationError e) {
      }
   }

   @Test
   public void
      givenValidRestScenarioOutlineRunner_whenGetNameIsCalled_thenNameIsReturned() {
      givenValidRestScenarioOutlineRunner();
      String name = restScenarioOutlineRunner.getName();
      assertEquals(MOCK_TEST_NAME, name);
   }

   @Test
   public void
      givenValidRestScenarioOutlineRunner_whenGetDescription_thenDescriptionIsReturned() {
      givenValidRestScenarioOutlineRunner();
      Description description = restScenarioOutlineRunner.getDescription();
      assertNotNull(description);
   }

}
