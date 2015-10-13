package rest;

import java.util.*;
import cucumber.runtime.model.CucumberFeature;

public class Summariser {
   private static final String SCENARIO = "Scenario:";
   private static final String SCENARIO_INTRO = "Scenario: This is an example";
   private Map<String, String> results;
   private Map<String, CucumberFeature> features;
   private RestRuntime runtime;
   private List<RestScenarioResult> restScenarioList;
   private List<CucumberFeatureResultContainer> containers;

   public Summariser(Map<String, String> results, Map<String, CucumberFeature> features,
      RestRuntime runtime) {
      this.results = results;
      this.features = features;
      this.runtime = runtime;
      containers = new ArrayList<CucumberFeatureResultContainer>();
      runReport();
   }

   public List<RestScenarioResult> getScenarioResults() {
      return Collections.unmodifiableList(restScenarioList);
   }

   public List<CucumberFeatureResultContainer> getContainers() {
      return containers;
   }

   private Map<String, CucumberFeature> createMapOfScenarios() {
      Map<String, CucumberFeature> scenarios = new HashMap<String, CucumberFeature>();
      for (String k : features.keySet()) {
         String scenario = getScenarioString(k);
         scenarios.put(scenario, features.get(k));
      }
      return scenarios;
   }

   private void runReport() {
      List<String> resultList = new ArrayList<String>();
      Set<String> r = results.keySet();
      List<String> res = new ArrayList<String>(r);
      for (int i = 0; i < res.size(); i++) {
         String key = res.get(i);
         String step = "[" + i + "]" + getStepString(key);
         String scenario = getScenarioString(key);
         String result = results.get(key);
         String resultString = "[" + scenario + "],[" + step + "],[" + result + "]";
         resultList.add(resultString);
      }
      createRestResultList(resultList);
   }

   private void createRestResultList(List<String> results) {
      Map<String, CucumberFeature> scenarios = createMapOfScenarios();
      restScenarioList = new ArrayList<RestScenarioResult>();

      for (String scenario : scenarios.keySet()) {
         RestScenarioResult restScenarioResult =
            createRestResultForScenario(scenario, results);
         restScenarioList.add(restScenarioResult);
         CucumberFeature cucumberFeature = scenarios.get(scenario);

         String issueKey = getTestIdsFromCucumberFeature(cucumberFeature);
         int index = checkIfContainerAlreadyExistsForTestId(issueKey);
         if (index == -1) {
            createNewContainer(cucumberFeature, restScenarioResult);
         } else {
            addToExistingContainer(index, restScenarioResult);
         }
      }
   }

   private String getTestIdsFromCucumberFeature(CucumberFeature cucumberFeature) {
      return cucumberFeature.getPath();
   }

   private int checkIfContainerAlreadyExistsForTestId(String testId) {
      int index = -1;
      for (int i = 0; i < containers.size(); i++) {
         CucumberFeatureResultContainer c = containers.get(i);
         if (testId.equals(c.getTestId())) {
            index = i;
         }
      }
      return index;
   }

   private void createNewContainer(CucumberFeature cucumberFeature,
      RestScenarioResult restScenarioResult) {
      CucumberFeatureResultContainer aNewContainer =
         new CucumberFeatureResultContainer(cucumberFeature);
      aNewContainer.addResult(restScenarioResult);
      containers.add(aNewContainer);
      runtime.addContainer(aNewContainer);
   }

   private void addToExistingContainer(int index, RestScenarioResult restScenarioResult) {
      CucumberFeatureResultContainer aExistingContainer = containers.get(index);
      aExistingContainer.addResult(restScenarioResult);
   }

   private RestScenarioResult createRestResultForScenario(String scenario,
      List<String> results) {
      Map<String, String> stepResultMap = new HashMap<String, String>();
      for (String s : results) {
         String[] scenarioStepResultData = s.split(",");
         String scenarioName = scenarioStepResultData[0];
         scenarioName = scenarioName.replace("[", "");
         scenarioName = scenarioName.replace("]", "");
         if (scenarioName.equalsIgnoreCase(scenario)) {
            String step = scenarioStepResultData[1];
            String result = scenarioStepResultData[2];
            stepResultMap.put(step, result);
         }
      }
      return new RestScenarioResult(scenario, stepResultMap);
   }

   private String getStepString(String s) {
      if (!s.contains(SCENARIO)) {
         s = SCENARIO_INTRO + s;
      }
      String[] array = s.split(SCENARIO);
      return array[0];
   }

   private String getScenarioString(String s) {
      if (!s.contains(SCENARIO)) {
         s = SCENARIO_INTRO + s;
      }
      String[] array = s.split(SCENARIO);
      return SCENARIO + " " + array[1];
   }
}
