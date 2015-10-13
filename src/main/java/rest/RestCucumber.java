package rest;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.*;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import cucumber.api.CucumberOptions;
import cucumber.runtime.ClassFinder;
import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;
import cucumber.runtime.junit.Assertions;
import cucumber.runtime.model.CucumberFeature;

/**
 * <p> Classes annotated with {@code @RunWith(RestCucumber.class)} will run a Cucumber
 * Feature. The class should be empty without any fields or methods. </p> <p> Cucumber
 * will look for a rest client class provided in the {@link RestCucumberOptions}
 * annotation. </p> Additional hints can be given to Cucumber by annotating the class with
 * {@link CucumberOptions}.
 * @see RestCucumberOptions
 * @see CucumberOptions
 */
public class RestCucumber extends ParentRunner<RestFeatureRunner> {
   private final RestJUnitReporter jUnitReporter;
   private final List<RestFeatureRunner> children = new ArrayList<RestFeatureRunner>();
   private final RestRuntime runtime;
   private final List<CucumberFeature> cucumberFeatures;
   private CucumberRestClient restClient = null;
   private boolean uploadResultEnabled = true;

   /**
    * Constructor called by JUnit.
    * @param clazz the class with the @RunWith annotation.
    * @throws java.io.IOException if there is a problem
    * @throws org.junit.runners.model.InitializationError if there is another problem
    * @throws CucumberInitException if the rest client fails to load
    */
   public RestCucumber(Class<?> clazz) throws InitializationError, IOException {
      super(clazz);
      ClassLoader classLoader = clazz.getClassLoader();
      Assertions.assertNoCucumberAnnotatedMethods(clazz);
      RestRuntimeOptionsFactory runtimeOptionsFactory =
         new RestRuntimeOptionsFactory(clazz);
      RestRuntimeOptions runtimeOptions = runtimeOptionsFactory.create();
      RestMultiLoader resourceLoader = new RestMultiLoader(classLoader);
      createRestClient(clazz);
      if (restClient != null) {
         resourceLoader.setRestClient(restClient);
      }
      runtime =
         createRuntime(resourceLoader, classLoader, runtimeOptions.getRuntimeOptions());
      cucumberFeatures = runtimeOptions.cucumberFeatures(resourceLoader);
      jUnitReporter =
         new RestJUnitReporter(runtimeOptions.reporter(classLoader),
            runtimeOptions.formatter(classLoader), runtimeOptions.isStrict(), runtime);
      addChildren(cucumberFeatures);
   }

   private void createRestClient(Class<?> clazz) {
      Annotation annotation = clazz.getAnnotation(RestCucumberOptions.class);
      if (annotation instanceof RestCucumberOptions) {
         RestCucumberOptions restCucumberOptions = (RestCucumberOptions) annotation;
         Class<?> restClientClass = restCucumberOptions.restClient();
         uploadResultEnabled = restCucumberOptions.uploadTestResults();
         ensureRestClientImplementsCucumberRestClient(restClientClass);
         loadRestClientClass(restClientClass);
         if (restClient == null) {
            throw new CucumberInitException(
               "Rest client is null. Please ensure your client built correctly.",
               new NullPointerException());
         }
      }
   }

   private void loadRestClientClass(Class<?> restClientClass) {
      try {
         Constructor<?> ctor = restClientClass.getConstructor();
         Object object = ctor.newInstance(new Object[] {});
         restClient = (CucumberRestClient) object;
      } catch (CucumberInitException e) {
         throw new CucumberInitException(e);
      } catch (ReflectiveOperationException e) {
         throw new CucumberInitException(e);
      }
   }

   public void ensureRestClientImplementsCucumberRestClient(Class<?> restClientClass) {
      String cucumberRestClientInterfaceName = "CucumberRestClient";
      Class<?>[] interfacesImplementedByClass = restClientClass.getInterfaces();
      Set<String> interfaceNames = new HashSet<String>();
      for (int i = 0; i < interfacesImplementedByClass.length; i++) {
         interfaceNames.add(interfacesImplementedByClass[i].getSimpleName());
      }
      if (!interfaceNames.contains(cucumberRestClientInterfaceName)) {
         throw new CucumberInitException(
            "Rest Client provided does not implement CucumberRestClient interface.");
      }
   }

   /**
    * Create the RestRuntime. Can be overridden to customize the runtime or backend.
    * @param resourceLoader used to load resources
    * @param classLoader used to load classes
    * @param runtimeOptions configuration
    * @return a new runtime
    * @throws InitializationError if a JUnit error occurred
    * @throws IOException if a class or resource could not be loaded
    */
   protected RestRuntime createRuntime(ResourceLoader resourceLoader,
      ClassLoader classLoader, RuntimeOptions runtimeOptions) throws InitializationError,
      IOException {
      ClassFinder classFinder =
         new ResourceLoaderClassFinder(resourceLoader, classLoader);
      return new RestRuntime(resourceLoader, classFinder, classLoader, runtimeOptions);
   }

   @Override
   public List<RestFeatureRunner> getChildren() {
      return children;
   }

   @Override
   public Description describeChild(RestFeatureRunner child) {
      return child.getDescription();
   }

   @Override
   protected void runChild(RestFeatureRunner child, RunNotifier notifier) {
      child.run(notifier);
   }

   @Override
   public void run(RunNotifier notifier) {
      super.run(notifier);
      jUnitReporter.done();
      if (restClient != null) {
         jUnitReporter.closeWithSummariser();
      } else {
         jUnitReporter.close();
      }
      if (uploadResultEnabled) {
         uploadResultsToRestClient();
      }
      printSummaryOfMissingStepsIfAny();
   }

   private void uploadResultsToRestClient() {
      List<CucumberFeatureResultContainer> resultContainers = runtime.getResults();
      for (CucumberFeatureResultContainer aResultContainer : resultContainers) {
         String testId = aResultContainer.getTestId();
         String status = aResultContainer.getFeatureResult();
         String comment = aResultContainer.getComment();
         Map<String, String> stepResults = aResultContainer.getStepResults();
         ResultOutput result = new ResultOutput(testId, status, comment, stepResults);
         restClient.updateTest(result);
      }
   }

   private void printSummaryOfMissingStepsIfAny() {
      Runtime runtimeWithSummary = runtime.getRuntime();
      runtimeWithSummary.printSummary();
   }

   private void addChildren(List<CucumberFeature> cucumberFeatures)
      throws InitializationError {
      for (CucumberFeature cucumberFeature : cucumberFeatures) {
         children.add(new RestFeatureRunner(cucumberFeature, runtime, jUnitReporter));
      }
   }
}
