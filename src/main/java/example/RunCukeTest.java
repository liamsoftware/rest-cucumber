package example;

import org.junit.runner.RunWith;
import rest.RestCucumber;
import rest.RestCucumberOptions;

@RunWith(RestCucumber.class)
@RestCucumberOptions(restClient = RestClientExample.class, uploadTestResults = true)
public class RunCukeTest {
}
