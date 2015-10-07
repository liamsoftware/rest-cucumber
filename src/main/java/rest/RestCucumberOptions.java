package rest;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface RestCucumberOptions {
   /**
    * @return a new Rest Client
    */
   String restClient() default "";

   String path() default "";

   boolean uploadResultEnabled() default true;
}