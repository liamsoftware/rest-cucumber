package rest;

import cucumber.runtime.RuntimeOptionsFactory;

public class RestRuntimeOptionsFactory {
   private RuntimeOptionsFactory runtimeOptionsFactory;

   public RestRuntimeOptionsFactory(Class<?> clazz) {
      runtimeOptionsFactory = new RuntimeOptionsFactory(clazz);
   }

   public RestRuntimeOptions create() {
      return new RestRuntimeOptions(runtimeOptionsFactory.create());
   }
}