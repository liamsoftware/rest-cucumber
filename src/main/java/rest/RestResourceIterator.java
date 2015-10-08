package rest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import cucumber.runtime.io.Resource;

public class RestResourceIterator implements Iterator<Resource> {
   private List<RestResource> testCases;
   private Iterator<RestResource> aTestCaseIt;

   public RestResourceIterator(List<RestResource> testCases) {
      this.testCases = new ArrayList<RestResource>(testCases);
      aTestCaseIt = this.testCases.iterator();
   }

   public boolean hasNext() {
      return aTestCaseIt.hasNext();
   }

   public Resource next() {
      return (Resource) aTestCaseIt.next();
   }

   public void remove() {
      aTestCaseIt.remove();
   }
}