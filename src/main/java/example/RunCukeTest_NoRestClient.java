package example;

import org.junit.runner.RunWith;
import rest.RestCucumber;
import cucumber.api.CucumberOptions;

/* This is an example of running a cucumber test the old way (with the feature files
 * stored locally. */
@RunWith(RestCucumber.class)
@CucumberOptions(features = "src/main/java/example")
public class RunCukeTest_NoRestClient {

}