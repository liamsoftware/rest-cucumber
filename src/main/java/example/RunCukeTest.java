package example;

import org.junit.runner.RunWith;
import rest.RestCucumber;
import rest.RestCucumberOptions;

@RunWith(RestCucumber.class)
@RestCucumberOptions(restClient = "example.MockRestClient",
   pathToProperties = "client.properties", uploadTestResults = false)
public class RunCukeTest {
}
