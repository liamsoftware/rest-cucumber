package rest;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;

public class RestRuntimeOptionsFactoryTest {

   private RestRuntimeOptionsFactory runtimeOptionsFactory;

   @Test
   public void givenClass_whenCtor_thenNoIssues() {
      givenRuntime();
      assertNotNull(runtimeOptionsFactory);
   }

   private void givenRuntime() {
      Class<?> clazz = this.getClass();
      runtimeOptionsFactory = new RestRuntimeOptionsFactory(clazz);
   }

   @Test
   public void givenRestRuntime_whenCreateCalled_thenRestRuntimeOptionsReturned() {
      givenRuntime();
      RestRuntimeOptions runtimeOptions = runtimeOptionsFactory.create();
      assertNotNull(runtimeOptions);
   }
}
