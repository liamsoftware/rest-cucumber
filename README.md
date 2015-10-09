## RestCucumber: Test .feature Files Stored in the Cloud

RestCucumber allows for execution of Cucumber BDD tests on feature files that are not stored locally. This project can be used with a Rest Client to GET the required feature files from a remote store (e.g. JIRA, HPs ALM, etc). A Rest Client implementing the Cucumber Rest interface is required to work with this tool.

* This tool wraps the [Cucumber-JVM](https://github.com/cucumber/cucumber-jvm) project
* For more info on Cucumber BDD visit [cukes.info](https://cucumber.io/)

## Code Example

Create a class to launch your Cucumber tests. 
* Include the RestCucumber.class as the @RunWith option
* Include the @RestCucumberOptions annotation which defines 
	* The Rest Client class that RestCucumber will load
	* A flag to indicate if test results should be posted back to the Rest Client
* See the [sample package](https://github.com/LiamHayes1/rest-cucumber/tree/master/src/main/java/example) including [RunCukeTest](https://github.com/LiamHayes1/rest-cucumber/blob/master/src/main/java/example/RunCukeTest.java)

```
import jira.RestCucumber;
import jira.RestCucumberOptions;
import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;

@RunWith(RestCucumber.class)
@RestCucumberOptions(restClient = RestClientExample.class, uploadTestResults = false)
public class RunCukeTest {
}
```

## Motivation

Storing feature files on developers local machines means that there can be a number of different versions of the same feature. Storing the feature files in a central tool (e.g. JIRA) means that everyone from developers, QA, and product managers can all work from the same feature file. Running the Cucumber BDD tests based on the feature files stored on a central tool helps speed up identifying when a feature is missing / failing from the build.

## Installation

Clone the repo or add the release version as a Maven dependency to your project:

```
<dependency>
    <groupId>com.github.liamhayes1</groupId>
    <artifactId>rest-cucumber</artifactId>
    <version>1.0</version>
</dependency>
```

## API Reference

CucumberRestClient interface implements 3 method signatures and must be included in any RestClient being used with RestCucumber.
```
String getFeatureString(String testId)
boolean updateTest(ResultOutput resultOutput)
Set<String> getTestIds()
```

## Tests

See the sample package for an example of using RestCucumber with a mock rest client.

## Contributors

Contact me if you would like to help with this project. 

## License

MIT