package rest;

import java.lang.annotation.*;
import example.RestClientExample;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface RestCucumberOptions {
   /**
    * @return a new Rest Client
    */
   Class<?> restClient() default RestClientExample.class;

   boolean uploadTestResults() default true;
}