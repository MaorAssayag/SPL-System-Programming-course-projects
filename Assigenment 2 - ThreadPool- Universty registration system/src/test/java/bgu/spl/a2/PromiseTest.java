package bgu.spl.a2;

import junit.framework.TestCase;
import java.util.concurrent.atomic.AtomicInteger;

public class PromiseTest extends TestCase {
	
	/**
	 * @Test 1 - Test Promise class - {@link #get()} function
	 */
	public final void testGet() throws Exception{
		 try{			 
	        Promise<Integer> p = new Promise<Integer>();
	        assertEquals(false,p.isResolved());
	        //part 1
	        try{
	            p.get();
	            fail("Test 1: Promise class - get() function - fail -  promise return value even that isnt resolved yet");
	        }
	        catch(IllegalStateException ex){
	        	System.out.println("Test 1: Promise class - get() function - part 1 - success");
	        }
	        
	        //part 2
	        try {p.resolve(1);}
	        catch(IllegalStateException ex){ // p has never been resolved
	        	fail("Test 1: Promise class - get() function - part 2 - failed - not accepting resolve() on object that never been resolved");
	        }
	        System.out.println("Test 1: Promise class - get() function - part 2 - success");
	        
	        //part 3
            int currentValue = -1;
	        try {currentValue = p.get();}
	        catch(IllegalStateException e){ // p has been resolved, should not throw IllegalStateException.
	        	fail("Test 1: Promise class - get() function - part 3 - failed - should not throw IllegalStateException");
	        }
            assertEquals(1,currentValue);
            
            //end of the tests
	        System.out.println("Test 1: Promise class - get() function - part 3 - success");
		} 
        catch(Exception ex){ // p has been resolved, should not throw IllegalStateException.
        	fail("Test 1: Promise class - get() function - failed");
        }
	}

	
	/**
	 * @Test 2 - Test Promise class - {@link #isResolved()} function
	 */
	public final void testIsResolved() throws Exception{
		try{
	        Promise<Integer> p = new Promise<Integer>();
	        assertEquals(false,p.isResolved());
	        
	        //part 1 - try to resolve for the 1st time
	        try{ p.resolve(1); }
	        catch(Exception ex){
	        	fail("Test 2: Promise class - isResolved() function - part 1 - failed to resolve" + ex.getMessage());
	        }
        	System.out.println("Test 2: Promise class - isResolved() function - part 1 - success");
        	
	        //part 2 - check if the status has been update
	        try {assertEquals(true,p.isResolved());}
	        catch(Exception ex){ // p has never been resolved
	        	fail("Test 2: Promise class - isResolved() function - part 2 - failed - not accepting resolve() on object that have been resolved");
	        }
	        System.out.println("Test 2: Promise class - isResolved() function - part 2 - success");
	       
	        //part 3 - try to resolve 2nd time
	        try{ 
	        	p.resolve(2);
	        	fail("Test 2: Promise class - isResolved() function - part 3 - failed (success to resolve again)");
	        }
	        catch(Exception ex){
	        	System.out.println("Test 2: Promise class - isResolved() function - part 3 - success");
	        }
	        
	        //part 4 - check if the status has been update again
	        try {assertEquals(true,p.isResolved());}
	        catch(Exception ex){ // p has never been resolved
	        	fail("Test 2: Promise class - isResolved() function - part 2 - failed - not accepting resolve() on object that have been resolved");
	        }
	        System.out.println("Test 2: Promise class - isResolved() function - part 4 - success");

            //end of the tests
	        System.out.println("Test 2: Promise class - isResolved() function - done successfully !");
		} 
        catch(Exception ex){ 
        	fail("Test 2: Promise class - isResolved() function - failed" + ex.getMessage());
        }
	}

	/**
	 * @Test 3 - Test Promise class - {@link #resolve()} function
	 */
	public final void testResolve() throws Exception{
		try{
	        Promise<Integer> p = new Promise<Integer>();
	        assertEquals(false,p.isResolved());
	        
	        //part 1 - try to resolve for the 1st time
	        try{ p.resolve(1); }
	        catch(Exception ex){
	        	fail("Test 3: Promise class - resolve() function - part 1 - failed to resolve 1st time" + ex.getMessage());
	        }
        	System.out.println("Test 3: Promise class - resolve() function - part 1 - success");
        	
	        //part 2 - check if the status has been update
	        try {assertEquals(true,p.isResolved());}
	        catch(Exception ex){ // p has never been resolved
	        	fail("Test 3: Promise class - resolve() function - part 2 - failed - not accepting resolve() on object that have been resolved");
	        }
	        System.out.println("Test 3: Promise class - resolve() function - part 2 - success");
	        
	        //part 3 - check if the value has been update with get
	        try {assertEquals(1, (int)p.get());}
	        catch(Exception ex){ // p has never been resolved
	        	fail("Test 3: Promise class - resolve() function - part 3 - failed - value isnt been updated");
	        }
	        System.out.println("Test 3: Promise class - resolve() function - part 3 - success");

	        //part 4 - try to resolve 2nd time
	        try{ 
	        	p.resolve(2);
	        	fail("Test 3: Promise class - resolve() function - part 4 - failed (success to resolve again)");
	        }
	        catch(Exception ex){
	        	System.out.println("Test 3: Promise class - resolve() function - part 4 - success");
	        }
	        
	      //part 5 - detach callback's
	        Promise<Integer> p2 = new Promise<Integer>();
	        AtomicInteger temp = new AtomicInteger(0);
            p2.subscribe(()->{
                temp.set(1);;
            });
            p2.resolve(2);
            synchronized (this) { // make sure that the thread finished - we assume that the environment is non-busy by external usage
				this.wait(200);
			}
            assertEquals(1, temp.get());
        	System.out.println("Test 3: Promise class - resolve() function - part 5 - success");
        	
            //end of the tests
	        System.out.println("Test 3: Promise class - resolve() function - done successfully !");
		} 
        catch(Exception ex){ 
        	fail("Test 3: Promise class - resolve() function - failed" + ex.getMessage());
        }
	}

	/**
	 * @Test 4 - Test Promise class - {@link #subscribe()} function
	 */
	public final void testSubscribe() throws Exception{
		try{
	        Promise<Integer> p = new Promise<Integer>();
	        Promise<Integer> p2 = new Promise<Integer>();
	        Promise<Integer> p3 = new Promise<Integer>();
	        AtomicInteger temp = new AtomicInteger(0);

	        //part 1 - try to resolve for the 1st time
	        try{ p.resolve(1); }
	        catch(Exception ex){
	        	fail("Test 4: Promise class - subscribe() function - part 1 - failed to resolve 1st time" + ex.getMessage());
	        }
        	System.out.println("Test 4: Promise class - subscribe() function - part 1 - success");
        	
	        //part 2 - callback should be execute immediately 
	        try {
	            p.subscribe(()->{
	                temp.set(1);;
	            });
	        }
	        catch(Exception ex){
	        	fail("Test 4: Promise class - subscribe() function - part 2 - failed - issue with subscribe() for the 1st");
	        }
            synchronized (this) { // make sure that the thread finished - we assume that the environment is non-busy by external usage
				this.wait(200);
			}
	        assertEquals(1, temp.get());
	        System.out.println("Test 4: Promise class - subscribe() function - part 2 - success");
	        
	        //part 3 - resolve with callback waiting
	        try {
	            p2.subscribe(()->{
	                temp.addAndGet(1);;
	            });
	            assertEquals(1, temp.get()); // the callback souldn't be execute only after resolve
	            p2.resolve(3); //maybe resolve will fail when there is a callback waiting
	        }
	        catch(Exception ex){ // p has never been resolved
	        	fail("Test 4: Promise class - subscribe() function - part 3 - failed - resolve() wite callback already waiting");
	        }
            synchronized (this) { // make sure that the thread finished - we assume that the environment is non-busy by external usage
				this.wait(200);
			}
	        assertEquals(2, temp.get()); // callback should do temp+1
	        System.out.println("Test 4: Promise class - subscribe() function - part 3 - success");

	        //part 4 - callback should be execute immediately 
	        try {
	            p2.subscribe(()->{
	                temp.addAndGet(1);;
	            });
	        }
	        catch(Exception ex){
	        	fail("Test 4: Promise class - subscribe() function - part 4 - failed - issue with subscribe() with resolved promise");
	        }
            synchronized (this) { // make sure that the thread finished - we assume that the environment is non-busy by external usage
				this.wait(200);
			}
	        assertEquals(3, temp.get());
	        System.out.println("Test 4: Promise class - subscribe() function - part 4 - success");
	        
	        //part 5 - callback shouldn't be execute again
	        try {
	        	p2.resolve(3);
	        }
	        catch(IllegalStateException ex){
		        System.out.println("Test 4: Promise class - subscribe() function - part 5.a - success");
	        }
	        assertEquals(3, temp.get());
	        System.out.println("Test 4: Promise class - subscribe() function - part 5.b - success");

	        //part 6 - a bunch of callback's need to be execute after resolve
	        temp.set(0); // set the atomic integer to 0
	        try {
	            p3.subscribe(()->{
	                temp.addAndGet(1);;
	            });
	            p3.subscribe(()->{
	                temp.addAndGet(1);;
	            });
		        assertEquals(0, temp.get());//the callback souldn't be execute only after resolve
	            p3.resolve(1);
	        }
	        catch(Exception ex){
		        fail("Test 4: Promise class - subscribe() function - part 6 - failed" + ex.getMessage());
	        }
            synchronized (this) { // make sure that the thread finished - we assume that the environment is non-busy by external usage
				this.wait(400);
			}
	        assertEquals(2, temp.get());
	        System.out.println("Test 4: Promise class - subscribe() function - part 6 - success");

	        //end of the tests
	        System.out.println("Test 4: Promise class - subscribe() function - done successfully !");
		} 
        catch(Exception ex){ 
        	fail("Test 4: Promise class - subscribe() function - failed" + ex.getMessage());
        }
	}
	
	/**
	 * EOF
	 */
}
