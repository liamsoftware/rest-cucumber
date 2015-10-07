package rest;

import java.util.ArrayList;
import java.util.List;
import cucumber.runtime.ClassFinder;
import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.model.CucumberFeature;

public class RestRuntime {
   private CucumberFeature currentCucumberFeature;
   private List<CucumberFeatureResultContainer> results;
   private Runtime runtime;

   public RestRuntime(ResourceLoader resourceLoader, ClassFinder classFinder,
      ClassLoader classLoader, RuntimeOptions runtimeOptions) {
      runtime = new Runtime(resourceLoader, classFinder, classLoader, runtimeOptions);
      results = new ArrayList<CucumberFeatureResultContainer>();
   }

   public RestRuntime(Runtime runtime) {
      this.runtime = runtime;
      results = new ArrayList<CucumberFeatureResultContainer>();
   }

   public void setCurrentCucumberFeature(CucumberFeature cucumberFeature) {
      currentCucumberFeature = cucumberFeature;
   }

   public CucumberFeature getCurrentCucumberFeature() {
      return currentCucumberFeature;
   }

   public void addContainer(CucumberFeatureResultContainer container) {
      results.add(container);
   }

   public List<CucumberFeatureResultContainer> getResults() {
      return results;
   }

   public Runtime getRuntime() {
      return runtime;
   }
}
