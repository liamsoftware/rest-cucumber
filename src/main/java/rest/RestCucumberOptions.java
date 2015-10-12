package rest;

import java.lang.annotation.*;
import example.RestClientExample;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface RestCucumberOptions {
   /**
    * @return a new Rest Client which implements the CucumberRestClient interface
    */
   Class<?> restClient() default RestClientExample.class;

   /**
    * @return true if the rest client is to upload test results, false if not
    */
   boolean uploadTestResults() default true;
}