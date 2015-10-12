package rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class RestResourceIteratorTest {

   private RestResourceIterator it;
   private static final String ISSUE_KEY = "TEST-123";
   private static final String BASE_URL = "www.base.url";

   @Test
   public void whenResourceIteratorIsCreated_andHasNextIsCalled_thenTrueIsReturned() {
      RestResource jr = new RestResource(ISSUE_KEY, BASE_URL);
      List<RestResource> jrList = new ArrayList<RestResource>();
      jrList.add(jr);
      it = new RestResourceIterator(jrList);
      assertTrue(it.hasNext());
   }

   @Test
   public void whenResourceIteratorIsCreated_andNextIsCalled_thenResourceIsReturned() {
      RestResource jr = new RestResource(ISSUE_KEY, BASE_URL);
      List<RestResource> jrList = new ArrayList<RestResource>();
      jrList.add(jr);
      it = new RestResourceIterator(jrList);
      RestResource jrFromIterator = (RestResource) it.next();
      assertEquals(jrFromIterator.testId, ISSUE_KEY);
   }

   @Test
   public void whenResourceIteratorIsCreated_andRemoveIsCalled_thenTheIteratorIsEmpty() {
      RestResource jr = new RestResource(ISSUE_KEY, BASE_URL);
      List<RestResource> jrList = new ArrayList<RestResource>();
      jrList.add(jr);
      it = new RestResourceIterator(jrList);
      it.next();
      it.remove();
      assertTrue(!it.hasNext());
   }
}
