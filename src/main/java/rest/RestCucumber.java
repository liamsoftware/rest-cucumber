package rest;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
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
 * <p> Classes annotated with {@code @RunWith(Cucumber.class)} will run a Cucumber
 * Feature. The class should be empty without any fields or methods. </p> <p> Cucumber
 * will look for a {@code .feature} file on the classpath, using the same resource path as
 * the annotated class ({@code .class} substituted by {@code .feature}). </p> Additional
 * hints can be given to Cucumber by annotating the class with {@link CucumberOptions}.
 * @see CucumberOptions
 */
public class RestCucumber extends ParentRunner<RestFeatureRunner> {
   private final RestJUnitReporter jUnitReporter;
   private final List<RestFeatureRunner> children = new ArrayList<RestFeatureRunner>();
   private final RestRuntime runtime;
   private final List<CucumberFeature> cucumberFeatures;
   private Class<?> restClientCreatedAtRuntime;
   private CucumberRestClient restClient = null;
   private boolean uploadResultEnabled = true;

   /**
    * Constructor called by JUnit.
    * @param clazz the class with the @RunWith annotation.
    * @throws java.io.IOException if there is a problem
    * @throws org.junit.runners.model.InitializationError if there is another problem
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
         RestCucumberOptions jcOptions = (RestCucumberOptions) annotation;
         String clientName = jcOptions.restClient();
         String pathToPropertiesFile = jcOptions.path();
         uploadResultEnabled = jcOptions.uploadResultEnabled();

         try {
            restClientCreatedAtRuntime = Class.forName(clientName);
         } catch (ClassNotFoundException e) {
            throw new CucumberInitException(
               "Rest client class not found. Ensure class name is correct and on the classpath",
               e);
         }

         Method method = null;
         try {
            method = restClientCreatedAtRuntime.getMethod("buildClient", String.class);
            restClient = (CucumberRestClient) method.invoke(null, pathToPropertiesFile);
         } catch (IllegalAccessException e) {
            throw new CucumberInitException("Rest client illegal access.", e);
         } catch (NoSuchMethodException e) {
            throw new CucumberInitException("Rest client no method exists.", e);
         } catch (SecurityException e) {
            throw new CucumberInitException("Rest client security error.", e);
         } catch (IllegalArgumentException e) {
            throw new CucumberInitException("Rest client illegal args.", e);
         } catch (InvocationTargetException e) {
            throw new CucumberInitException("Rest client invocation failed.", e);
         }

         if (restClient == null) {
            throw new CucumberInitException(
               "Rest client is null. Please ensure your client built correctly.",
               new NullPointerException());
         }
      }
   }

   /**
    * Create the Runtime. Can be overridden to customize the runtime or backend.
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
         List<CucumberFeatureResultContainer> summaryList = runtime.getResults();
         for (CucumberFeatureResultContainer container : summaryList) {
            String issueKey = container.getIssueKey();
            String status = container.getFeatureResult();
            String comment = container.getComment();
            ResultOutput result =
               new ResultOutput(issueKey, status, comment, container.getStepResults());
            restClient.updateExecution(result);
         }
      }
      printSummaryOfMissingStepsIfAny();
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
