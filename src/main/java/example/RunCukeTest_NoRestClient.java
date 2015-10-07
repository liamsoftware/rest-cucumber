package example;

import org.junit.runner.RunWith;
import rest.RestCucumber;
import cucumber.api.CucumberOptions;

@RunWith(RestCucumber.class)
@CucumberOptions(features = "src/main/java/example")
public class RunCukeTest_NoRestClient {

}