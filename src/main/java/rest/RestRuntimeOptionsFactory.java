package rest;

import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.RuntimeOptionsFactory;

public class RestRuntimeOptionsFactory {
   private RuntimeOptionsFactory runtimeOptionsFactory;
   private RuntimeOptions runtimeOptions;

   public RestRuntimeOptionsFactory(Class<?> clazz) {
      runtimeOptionsFactory = new RuntimeOptionsFactory(clazz);
   }

   public RestRuntimeOptions create() {
      runtimeOptions = runtimeOptionsFactory.create();
      return new RestRuntimeOptions(runtimeOptions);
   }
}