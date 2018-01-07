package bgu.spl.a2;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Describes a monitor that supports the concept of versioning - its idea is
 * simple, the monitor has a version number which you can receive via the method
 * {@link #getVersion()} once you have a version number, you can call
 * {@link #await(int)} with this version number in order to wait until this
 * version number changes.
 *
 * you can also increment the version number by one using the {@link #inc()}
 * method.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class VersionMonitor {
	private AtomicInteger version = new AtomicInteger(0);
	
    public synchronized int getVersion() { //a Thread can't change the version value throw this methods, it doesn't need to be synchronize.
        return this.version.intValue();
    }

    public synchronized void inc() { // only 1 Thread at a time can change the value of version.
    	version.getAndIncrement();
    	this.notifyAll(); // for all the threads that waiting for the version to change or for inc() 
    }

    public synchronized void await(int version) throws InterruptedException { // only 1 Thread at a time can read the value of version at a time.
		while (this.getVersion() == version) { // avoid busy wait for the Thread
			this.wait();
		}
    }
    
    /*
     * End Of File
     */
}
