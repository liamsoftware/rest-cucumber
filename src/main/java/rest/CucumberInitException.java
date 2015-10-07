package rest;

public class CucumberInitException extends RuntimeException {
   private static final long serialVersionUID = 1L;

   public CucumberInitException(String message) {
      super(message);
   }

   public CucumberInitException(String message, Throwable cause) {
      super(message, cause);
   }

   public CucumberInitException(Throwable cause) {
      super(cause);
   }
}
