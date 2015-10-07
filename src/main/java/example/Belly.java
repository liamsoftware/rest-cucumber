package example;

public class Belly {
   private int cukesInBelly = 0;

   public void eat(int cukes) {
      cukesInBelly = cukes;
   }

   public void waitAnHour(int cukes) {
      cukesInBelly = cukes;
   }

   public void growl() {
      cukesInBelly = 10;
   }

   public int getCukesInBelly() {
      return cukesInBelly;
   }
}