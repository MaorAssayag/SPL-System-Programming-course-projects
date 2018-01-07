package bgu.spl.a2;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * this class represents a deferred result i.e., an object that eventually will
 * be resolved to hold a result of some operation, the class allows for getting
 * the result once it is available and registering a callback that will be
 * called once the result is available.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 *
 * @param <T>
 *            the result type, <boolean> resolved - initialized ;
 */
public class Promise<T>{
	private ConcurrentLinkedQueue<callback> callbackList = new ConcurrentLinkedQueue<callback>();
	private boolean resolved = false;
	private T value;
		
	/**
	 *
	 * @return the resolved value if such exists (i.e., if this object has been
	 *         {@link #resolve(java.lang.Object)}ed 
	 * @throws IllegalStateException
	 *             in the case where this method is called and this object is
	 *             not yet resolved
	 */
	public synchronized T get() { //<synchronized>: avoid 2 threads that can check the same promise and change the inner condition for the value.
		if (!isResolved()) {
			throw new IllegalStateException("Promise has not been resolved yet !");
		}
		else return this.value;
	}

	/**
	 *
	 * @return true if this object has been resolved - i.e., if the method
	 *         {@link #resolve(java.lang.Object)} has been called on this object
	 *         before.
	 */
	public boolean isResolved() {
		return this.resolved;
	}


	/**
	 * resolve this promise object - from now on, any call to the method
	 * {@link #get()} should return the given value
	 *
	 * Any callbacks that were registered to be notified when this object is
	 * resolved via the {@link #subscribe(callback)} method should
	 * be executed before this method returns
	 *
     * @throws IllegalStateException
     * 			in the case where this object is already resolved
	 * @param value
	 *            - the value to resolve this promise object with
	 */
	public synchronized void resolve(T value){ //<synchronized>: We allow only 1 Thread to change the inner value for promise.
		if (isResolved()) {
			throw new IllegalStateException("Promise has already been resolved!");
		}//else :
		this.value = value;
		this.resolved = true;
		while (!callbackList.isEmpty()) {
			callbackList.poll().call(); //executed the subscribed callback's
		}
	}

	/**
	 * add a callback to be called when this object is resolved. If while
	 * calling this method the object is already resolved - the callback should
	 * be called immediately
	 *
	 * Note that in any case, the given callback should never get called more
	 * than once, in addition, in order to avoid memory leaks - once the
	 * callback got called, this object should not hold its reference any
	 * longer.
	 *
	 * @param callback
	 *            the callback to be called when the promise object is resolved
	 */
	public synchronized void subscribe(callback callback) {//<synchronized>: avoid thread that try to subscribe and in the same time another Thread change the inner condition for the value.
		if (!isResolved()) {
			callbackList.add(callback);
		}else {
			callback.call();
		}	
	}
	
	/*
	 * End of File
	 */
}
