package rest;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import java.util.*;
import org.junit.Before;
import org.junit.Test;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.model.CucumberFeature;

public class RestRuntimeOptionsTest {

   private RuntimeOptions runtimeOptions = mock(RuntimeOptions.class);
   private ClassLoader classLoader = mock(ClassLoader.class);
   private RestMultiLoader jiraMultiLoader = new RestMultiLoader(classLoader);
   private RestRuntimeOptions jiraRuntimeOptions;
   private List<CucumberFeature> cucumberFeatures;
   private Reporter reporter = mock(Reporter.class);
   private Formatter formatter = mock(Formatter.class);
   private CucumberRestClient restClient = mock(CucumberRestClient.class);
   private String feature =
      "Feature: this is a feature Scenario a feature scenario Given something When something else Then return true";

   @Before
   public void setUp() {
      Set<String> issues = new HashSet<String>();
      issues.add("2322");
      when(restClient.getTestIds()).thenReturn(issues);
   }

   @Test
   public void whenJiraMultiLoaderProvided_thenCreateCucumberFeatures() {
      List<String> jiraList = new ArrayList<String>();
      jiraList.add("jira");
      List<Object> filterList = new ArrayList<Object>();
      when(runtimeOptions.getFeaturePaths()).thenReturn(jiraList);
      when(runtimeOptions.getFilters()).thenReturn(filterList);
      jiraRuntimeOptions = new RestRuntimeOptions(runtimeOptions);
      jiraMultiLoader.setRestClient(restClient);
      when(restClient.getFeatureString(anyString())).thenReturn(feature);
      cucumberFeatures = jiraRuntimeOptions.cucumberFeatures(jiraMultiLoader);
      assertTrue(cucumberFeatures != null);
   }

   @Test
   public void whenMultiLoaderProvided_thenCreateCucumberFeatures() {
      List<String> featureList = new ArrayList<String>();
      featureList.add("src/main/java/example");
      List<Object> filterList = new ArrayList<Object>();
      when(runtimeOptions.getFeaturePaths()).thenReturn(featureList);
      when(runtimeOptions.getFilters()).thenReturn(filterList);
      jiraRuntimeOptions = new RestRuntimeOptions(runtimeOptions);
      cucumberFeatures = jiraRuntimeOptions.cucumberFeatures(jiraMultiLoader);
      assertTrue(cucumberFeatures != null);
   }

   @Test
   public void whenReporterRequested_thenReturnReporter() {
      when(runtimeOptions.reporter(classLoader)).thenReturn(reporter);
      jiraRuntimeOptions = new RestRuntimeOptions(runtimeOptions);
      assertTrue(jiraRuntimeOptions.reporter(classLoader) != null);
   }

   @Test
   public void whenFormatterRequested_thenReturnFormatter() {
      when(runtimeOptions.formatter(classLoader)).thenReturn(formatter);
      jiraRuntimeOptions = new RestRuntimeOptions(runtimeOptions);
      assertTrue(jiraRuntimeOptions.formatter(classLoader) != null);
   }

   @Test
   public void whenIsStrictRequested_thenReturnIsStrict() {
      when(runtimeOptions.isStrict()).thenReturn(true);
      jiraRuntimeOptions = new RestRuntimeOptions(runtimeOptions);
      assertTrue(jiraRuntimeOptions.isStrict());
   }

   @Test
   public void whenRuntimeOptionsIsRequested_thenReturnRuntimeOptions() {
      jiraRuntimeOptions = new RestRuntimeOptions(runtimeOptions);
      RuntimeOptions runtimeOptionsFromJira = jiraRuntimeOptions.getRuntimeOptions();
      assertTrue(runtimeOptionsFromJira != null);
   }
}
