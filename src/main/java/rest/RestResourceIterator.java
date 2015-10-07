package rest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import cucumber.runtime.io.Resource;

public class RestResourceIterator implements Iterator<Resource> {
   private List<RestResource> issues;
   private Iterator<RestResource> i;

   public RestResourceIterator(List<RestResource> jiraIssues) {
      this.issues = new ArrayList<RestResource>(jiraIssues);
      i = issues.iterator();
   }

   public boolean hasNext() {
      return i.hasNext();
   }

   public Resource next() {
      return (Resource) i.next();
   }

   public void remove() {
      i.remove();
   }
}