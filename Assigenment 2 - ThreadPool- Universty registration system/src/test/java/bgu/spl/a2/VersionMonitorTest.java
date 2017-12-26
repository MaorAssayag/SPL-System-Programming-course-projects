package bgu.spl.a2;
import junit.framework.TestCase;
/**
 * VersionMonitor class - Describes a monitor that supports the concept of versioning - its idea is
 * simple, the monitor has a version number which you can receive via the method
 * {@link #getVersion()} once you have a version number, you can call
 * {@link #await(int)} with this version number in order to wait until this
 * version number changes. you can also increment the version number by one using the {@link #inc()}
 * method.
 * 
 * VersionMonitorTest class used for testing VersionMonitor class & function's.
 */

public class VersionMonitorTest extends TestCase {
	
	/**
	 * @Test 1 - Test VersionMonitor class - {@link #getVersion()} function
	 */
	public final void testGetVersion() throws Exception{
		 try {
			 VersionMonitor m = new VersionMonitor(); // assuming the intial version is 0
			 try { assertEquals(0,m.getVersion());
			 }
			 catch (Exception ex){
				 fail("Test 1: testGetVersion has been failed - get the current version of VersionMonitor");
			 }
			 System.out.println("Test 1: VersionMonitor class - getVersion() function - done successfully !");
		 }
		 catch (Exception ex){
			 fail("Test 1: testGetVersion has been failed"+ex.getMessage()) ;
		 }
	}

	/**
	 * @Test 2 - Test VersionMonitor class - {@link #inc()} function
	 */
	public final void testInc() throws Exception{
		try {
			 VersionMonitor m = new VersionMonitor(); // assuming the intial version is 0
			 for (int i = 0; i < 7; i++) {m.inc();}
			 try {assertEquals(7, m.getVersion());
			 }
			 catch (Exception ex){
				 fail("Test 2: testInc has been failed after getVersiom" + ex.getMessage());
			 }
			 m.inc();
			 assertEquals(8, m.getVersion());
			 m.inc();
			 assertEquals(9, m.getVersion());
			 System.out.println("Test 2: VersionMonitor class - inc() function - done successfully !");
		 }
		 catch (Exception ex){
			 fail("Test 2: VersionMonitor class - testInc has been failed after increment- " + ex.getMessage());
		 }
	}

	/**
	 * @Test 3 - - Test VersionMonitor class - {@link #await(int)} function
	 */
	public final void testAwait() throws Exception{
        VersionMonitor m = new VersionMonitor();
        Thread thread1 = new Thread(() -> {
            try {
                m.await(0);
            } catch (InterruptedException e) {e.printStackTrace();}
        });
        
         try {
        	 thread1.start();
        	 assertEquals(true, thread1.isAlive());
             m.inc();
             synchronized (this) { // make sure that the thread finished - we assume that the environment is non-busy by external usage
				this.wait(200);
			}
             if(thread1.isAlive()) {
            	 fail("Test 3: VersionMonitor class - await() function - fail - thread1 isnt finsih");
             }
             assertEquals(1,m.getVersion());
             thread1 = new Thread(() -> {
                 try {m.await(3);} // the function await should return immediately
                 catch (InterruptedException e) {e.printStackTrace();}
             });
             thread1.start();
             synchronized (this) { // make sure that the thread finished - we assume that the environment is non-busy by external usage
				this.wait(200);
			}
             assertEquals(false, thread1.isAlive());
             System.out.println("Test 3: VersionMonitor class - await() function - done successfully !");
        }
        catch(Exception ex){
            fail("Test 3: VersionMonitor class - await() function - fail" + ex.getMessage());
        }
	}
	
	/**
	 * EOF
	 */
}
