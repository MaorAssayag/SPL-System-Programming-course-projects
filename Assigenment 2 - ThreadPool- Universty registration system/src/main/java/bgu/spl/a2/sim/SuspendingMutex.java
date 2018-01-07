package bgu.spl.a2.sim;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import bgu.spl.a2.Promise;

/**
 * 
 * this class is related to {@link Computer}
 * it indicates if a computer is free or not
 * 
 * Note: this class can be implemented without any synchronization. 
 * However, using synchronization will be accepted as long as the implementation is blocking free.
 *
 */
public class SuspendingMutex {
	private Computer computer;
	AtomicBoolean isFree;
	ConcurrentLinkedQueue<Promise<Computer>> promiseList;
	
	/**
	 * Constructor
	 * @param computer
	 */
	public SuspendingMutex(Computer computer){
		this.computer = computer;
		this.isFree = new AtomicBoolean(true);
		this.promiseList = new ConcurrentLinkedQueue<Promise<Computer>>();
	}
	
	/**
	 * Computer acquisition procedure
	 * Note that this procedure is non-blocking and should return immediately.
	 * 
	 * @return a promise for the requested computer
	 */
	public Promise<Computer> down(){
		Promise<Computer> promise = new Promise<Computer>();
		if (this.isFree.getAndSet(false)) {
			promise.resolve(this.computer);
			return promise;
		}
		else this.promiseList.add(promise);
		
		if (this.isFree.getAndSet(false)) {
			this.promiseList.poll().resolve(this.computer);
		}
		return promise;
	}
	
	/**
	 * Computer return procedure
	 * releases a computer which becomes available in the warehouse upon completion
	 */
	public void up(){
		if(promiseList.isEmpty()) {
			this.isFree.set(true);
			if((!promiseList.isEmpty()) && (this.isFree.compareAndSet(true, false)))
				this.promiseList.poll().resolve(this.computer);
		}
		else this.promiseList.poll().resolve(this.computer);	
	}
	
	/*
	 * End of File.
	 */
}
