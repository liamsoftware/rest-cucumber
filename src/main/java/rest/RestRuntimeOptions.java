package rest;

import static rest.RestCucumberFeatureLoader.load;
import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import java.util.List;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.model.CucumberFeature;

public class RestRuntimeOptions {
   private RuntimeOptions runtimeOptions;

   public RestRuntimeOptions(RuntimeOptions runtimeOptions) {
      this.runtimeOptions = runtimeOptions;
   }

   public List<CucumberFeature> cucumberFeatures(ResourceLoader resourceLoader) {
      if (resourceLoader instanceof RestMultiLoader) {
         return load(resourceLoader, runtimeOptions.getFeaturePaths(),
            runtimeOptions.getFilters(), System.out);
      }
      return CucumberFeature.load(resourceLoader, runtimeOptions.getFeaturePaths(),
         runtimeOptions.getFilters());
   }

   public Reporter reporter(ClassLoader classLoader) {
      return runtimeOptions.reporter(classLoader);
   }

   public Formatter formatter(ClassLoader classLoader) {
      return runtimeOptions.formatter(classLoader);
   }

   public boolean isStrict() {
      return runtimeOptions.isStrict();
   }

   public RuntimeOptions getRuntimeOptions() {
      return runtimeOptions;
   }
}
