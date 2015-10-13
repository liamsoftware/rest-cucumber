package rest;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.*;
import cucumber.runtime.FeatureBuilder;
import cucumber.runtime.io.Resource;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.model.PathWithLines;

public class RestCucumberFeatureLoader {
   public static final String REST_CLIENT_KEY = "rest client";

   private RestCucumberFeatureLoader() {
   }

   public static List<CucumberFeature> load(ResourceLoader resourceLoader,
      List<String> featurePaths, List<Object> filters, PrintStream out) {
      if (resourceLoader instanceof RestMultiLoader) {
         RestMultiLoader restClientResourceLoader = (RestMultiLoader) resourceLoader;
         if (restClientResourceLoader.hasRestClientSet()) {
            List<String> newFeaturePaths = new ArrayList<String>();
            for (int i = 0; i < featurePaths.size(); i++) {
               newFeaturePaths.add(REST_CLIENT_KEY);
            }
            featurePaths = newFeaturePaths;
         }
      }
      final List<CucumberFeature> cucumberFeatures =
         load(resourceLoader, featurePaths, filters);
      if (cucumberFeatures.isEmpty()) {
         if (featurePaths.isEmpty()) {
            out.println(String.format("Got no path to feature directory or feature file"));
         } else if (filters.isEmpty()) {
            out.println(String.format("No features found at %s", featurePaths));
         } else {
            out.println(String.format(
               "None of the features at %s matched the filters: %s", featurePaths,
               filters));
         }
      }
      return cucumberFeatures;
   }

   public static List<CucumberFeature> load(ResourceLoader resourceLoader,
      List<String> featurePaths, List<Object> filters) {
      final List<CucumberFeature> cucumberFeatures = new ArrayList<CucumberFeature>();
      final FeatureBuilder builder = new FeatureBuilder(cucumberFeatures);
      for (String featurePath : featurePaths) {
         if (featurePath.equalsIgnoreCase(REST_CLIENT_KEY)) {
            loadFromRestClient(resourceLoader, builder, filters);
         } else {
            loadFromFeaturePath(builder, resourceLoader, featurePath, filters, false);
         }
      }
      Collections.sort(cucumberFeatures, new CucumberFeatureUriComparator());
      return cucumberFeatures;
   }

   private static void loadFromRestClient(ResourceLoader resourceLoader,
      FeatureBuilder builder, List<Object> filters) {
      Iterable<Resource> resources = resourceLoader.resources("", REST_CLIENT_KEY);
      if (!resources.iterator().hasNext()) {
         throw new IllegalArgumentException(
            "No resource found, please ensure you have set a rest client to use with JiraCucumber.");
      }
      for (Resource resource : resources) {
         builder.parse(resource, filters);
      }
   }

   private static void loadFromFeaturePath(FeatureBuilder builder,
      ResourceLoader resourceLoader, String featurePath, final List<Object> filters,
      boolean failOnNoResource) {
      PathWithLines pathWithLines = new PathWithLines(featurePath);
      List<Object> filtersForPath = new ArrayList<Object>(filters);
      filtersForPath.addAll(pathWithLines.lines);
      Iterable<Resource> resources =
         resourceLoader.resources(pathWithLines.path, ".feature");
      if (failOnNoResource && !resources.iterator().hasNext()) {
         throw new IllegalArgumentException("No resource found for: "
            + pathWithLines.path);
      }
      for (Resource resource : resources) {
         builder.parse(resource, filtersForPath);
      }
   }

   private static class CucumberFeatureUriComparator implements
      Comparator<CucumberFeature>, Serializable {
      private static final long serialVersionUID = 1L;

      public int compare(CucumberFeature a, CucumberFeature b) {
         return a.getPath().compareTo(b.getPath());
      }
   }
}
