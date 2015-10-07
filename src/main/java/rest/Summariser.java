package rest;

import java.util.*;
import cucumber.runtime.model.CucumberFeature;

public class Summariser {
   private Map<String, String> results;
   private Map<String, CucumberFeature> features;
   private RestRuntime runtime;
   private List<RestScenarioResult> jiraScenarioList;
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
      return jiraScenarioList;
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
      jiraScenarioList = new ArrayList<RestScenarioResult>();

      for (String scenario : scenarios.keySet()) {
         RestScenarioResult jiraScenarioResult =
            createRestResultForScenario(scenario, results);
         jiraScenarioList.add(jiraScenarioResult);
         CucumberFeature cucumberFeature = scenarios.get(scenario);

         String issueKey = getIssueKeyFromCucumberFeature(cucumberFeature);
         int index = checkIfContainerAlreadyExistsForIssueKey(issueKey);
         if (index == -1) {
            createNewContainer(cucumberFeature, jiraScenarioResult);
         } else {
            addToExistingContainer(index, jiraScenarioResult);
         }
      }
   }

   private String getIssueKeyFromCucumberFeature(CucumberFeature cucumberFeature) {
      String issueKey = cucumberFeature.getPath();
      if (!issueKey.contains(",")) {
         throw new IllegalArgumentException(
            "Issue key hass not been assigned to the cucumber feature correctly.");
      }
      issueKey = issueKey.split(",")[0];
      issueKey = issueKey.replace("[", "");
      return issueKey;
   }

   private int checkIfContainerAlreadyExistsForIssueKey(String issueKey) {
      int index = -1;
      for (int i = 0; i < containers.size(); i++) {
         CucumberFeatureResultContainer c = containers.get(i);
         if (issueKey.equals(c.getIssueKey())) {
            index = i;
         }
      }
      return index;
   }

   private void createNewContainer(CucumberFeature cucumberFeature,
      RestScenarioResult jiraScenarioResult) {
      CucumberFeatureResultContainer aNewContainer =
         new CucumberFeatureResultContainer(cucumberFeature);
      aNewContainer.addResult(jiraScenarioResult);
      containers.add(aNewContainer);
      runtime.addContainer(aNewContainer);
   }

   private void addToExistingContainer(int index, RestScenarioResult jiraScenarioResult) {
      CucumberFeatureResultContainer aExistingContainer = containers.get(index);
      aExistingContainer.addResult(jiraScenarioResult);
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
      if (!s.contains("Scenario:")) {
         s = "Scenario: This is an example" + s;
      }
      String[] array = s.split("Scenario:");
      return array[0];
   }

   private String getScenarioString(String s) {
      if (!s.contains("Scenario:")) {
         s = "Scenario: This is an example" + s;
      }
      String[] array = s.split("Scenario:");
      return "Scenario: " + array[1];
   }
}