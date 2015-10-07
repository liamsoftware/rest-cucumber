package example;

import org.junit.runner.RunWith;
import rest.RestCucumber;
import rest.RestCucumberOptions;

@RunWith(RestCucumber.class)
@RestCucumberOptions(restClient = "example.MockRestClient", path = "client.properties",
   uploadResultEnabled = false)
public class RunCukeTest {
}
