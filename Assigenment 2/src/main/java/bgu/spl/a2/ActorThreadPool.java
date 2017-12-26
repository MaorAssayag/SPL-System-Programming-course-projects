package bgu.spl.a2;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.HashMap;
import java.util.Map;

/**
 * represents an actor thread pool - to understand what this class does please
 * refer to your assignment.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class ActorThreadPool {
	
	private ConcurrentHashMap<String, ConcurrentLinkedQueue<Action<?>>> actorsQueues;
	private ConcurrentHashMap<String, PrivateState> actorsPrivateStates;
	private ConcurrentHashMap<String, Boolean> isTheActorLocked;
	private BlockingQueue<Thread> threadsQueue;
	private VersionMonitor currentVersion;//singelton
	private AtomicBoolean isRunning;
	
	/**
	 * creates a {@link ActorThreadPool} which has nthreads. Note, threads
	 * should not get started until calling to the {@link #start()} method.
	 *
	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 *
	 * @param nthreads
	 *            the number of threads that should be started by this thread
	 *            pool
	 */
	public ActorThreadPool(int nthreads) {
		this.actorsQueues = new ConcurrentHashMap<String, ConcurrentLinkedQueue<Action<?>>>(); //Initialize private data members of the pool - we choose not to use diamond operator for clearity.
		this.actorsPrivateStates = new ConcurrentHashMap<String, PrivateState>();
		this.isTheActorLocked = new ConcurrentHashMap<String, Boolean>();
		this.threadsQueue = new LinkedBlockingQueue<Thread>(nthreads);
		this.currentVersion = new VersionMonitor();
		this.isRunning = new AtomicBoolean(false); // until the start() method will be called
		
		for (int i = 0; i < nthreads; i++) { //lets add 'nthreads' threads
			this.threadsQueue.add(new Thread( ()-> ThreadCode()) ); // ThreadCode method - Thread individual code
		}
	}

	/**
	 * getter for actors
	 * @return actors
	 */
	public Map<String, PrivateState> getActors(){
		return this.actorsPrivateStates;
	}
	
	/**
	 * getter for actors; with hashMap
	 * @return actors
	 */
	public HashMap<String, PrivateState> getActorsHash(){
		HashMap<String, PrivateState> ans = new HashMap<String, PrivateState>(getActors());
		return ans;
	}
	
	public ConcurrentLinkedQueue<Action<?>> getActorQueue(String actorID){
		return this.actorsQueues.get(actorID);
	}
	
	/**
	 * getter for actor's private state
	 * @param actorId actor's id
	 * @return actor's private state
	 */
	public PrivateState getPrivateState(String actorId){
		return this.actorsPrivateStates.get(actorId);
	}

	/**
	 * submits an action into an actor to be executed by a thread belongs to
	 * this thread pool
	 *
	 * @param action
	 *            the action to execute
	 * @param actorId
	 *            corresponding actor's id
	 * @param actorState
	 *            actor's private state (actor's information)
	 */
	public void submit(Action<?> action, String actorId, PrivateState actorState) {
		if (!this.actorsQueues.containsKey(actorId)) {
			synchronized (this.isTheActorLocked) {
				if (!this.actorsQueues.containsKey(actorId)) {
					ConcurrentLinkedQueue<Action<?>> temp = new ConcurrentLinkedQueue<Action<?>>();
					this.actorsQueues.put(actorId, temp);
					this.isTheActorLocked.put(actorId, false); //this is a New Actor & he is available.
					this.actorsPrivateStates.put(actorId, actorState);
				}
			}
		}
		((ConcurrentLinkedQueue<Action<?>>)this.actorsQueues.get(actorId)).add(action);
		this.currentVersion.inc();
	}

	/**
	 * closes the thread pool - this method interrupts all the threads and waits
	 * for them to stop - it is returns *only* when there are no live threads in
	 * the queue.
	 *
	 * after calling this method - one should not use the queue anymore.
	 *
	 * @throws InterruptedException
	 *             if the thread that shut down the threads is interrupted
	 */
	public void shutdown() throws InterruptedException {
		this.isRunning.set(false);
		for(Thread t:this.threadsQueue) {
			t.interrupt(); // Interrupts this thread - maybe.  
		}
		for(Thread t: this.threadsQueue) {
			t.join(); // Waits for this thread to die. 
		}
		System.out.println("shutting down complete");
	}

	/**
	 * start the threads belongs to this thread pool
	 */
	public void start() {
		this.isRunning.set(true);
		for(Thread t:this.threadsQueue) {
			t.start(); // Interrupts this thread - maybe.  
		}
	}
	
	/**
	 * Thread individual code with the Loop design pattern
	 * isBusy : if the thread can find an action to execute, it should be busy - false after the action is done handling.
	 * currentAction : the current action to fetch & handle.
	 * synchronized : the isTheActorLocked queue for preventing 2 threads trying to handle the same action for the same actor queue.
	 * 
	 */
	private void ThreadCode() {
		Action<?> currentAction = null;
		int curVersion = 0;
		String currentActorId = "";
		boolean isBusy = false;
		while(!Thread.currentThread().isInterrupted()){
			synchronized(this.isTheActorLocked) {
				for (String id : this.actorsQueues.keySet()) {
					if (!this.isTheActorLocked.get(id) && !this.actorsQueues.get(id).isEmpty()) { //the actor is free to fetch action's from
						isBusy = true;
						currentActorId = id;
						this.isTheActorLocked.put(id, true);
						curVersion = this.currentVersion.getVersion();
						break;
					}
				}
			}// free the current actor queue for other threads
			if (isBusy) {
				try {
					currentAction = this.actorsQueues.get(currentActorId).poll();
					currentAction.handle(this, currentActorId, this.actorsPrivateStates.get(currentActorId));
				}catch(Exception e) {
					System.out.println("cant handle " + currentAction.getActionName() +" "+ currentActorId );
					e.printStackTrace();					
				}
				this.isTheActorLocked.put(currentActorId, false); // free this actor to other threads
				this.currentVersion.inc();
				isBusy = false;
			}
			else { //isBusy = false
				try { this.currentVersion.await(curVersion);} 
				catch (InterruptedException  e) {Thread.currentThread().interrupt();} // exception from await or from interrupt while the thread is waiting
			}
		}
	}
	
	/*
	 * End Of File.
	 */
}
