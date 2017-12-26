package bgu.spl.a2;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * an abstract class that represents an action that may be executed using the
 * {@link ActorThreadPool}
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add to this class can
 * only be private!!!
 *
 * @param <R> the action result type
 */
public abstract class Action<R> {
	
	protected ActorThreadPool pool;
	protected String actorId;
	protected PrivateState actorState;
	protected callback callback;
	protected String actionName;
	protected Promise<R> Result;
	protected AtomicInteger actionRemaining = new AtomicInteger(0);
	protected boolean canBeExecute = false;
	
	/**
     * start handling the action - note that this method is protected, a thread
     * cannot call it directly.
     */
    protected abstract void start();
    

    /**
    *
    * start/continue handling the action
    *
    * this method should be called in order to start this action
    * or continue its execution in the case where it has been already started.
    *
    * IMPORTANT: this method is package protected, i.e., only classes inside
    * the same package can access it - you should *not* change it to
    * public/private/protected
    *
    */
   /*package*/ final void handle(ActorThreadPool pool, String actorId, PrivateState actorState) {
	   this.pool = pool;
	   this.actorId = actorId;
	   this.actorState = actorState;
	   if (canBeExecute) { // then all the given actions results are resolved
		   callback.call();
		   canBeExecute = false;
	   }else {
		   this.start();
	   }	   
   }
    
    
    /**
     * add a callback to be executed once *all* the given actions results are
     * resolved
     * 
     * Implementors note: make sure that the callback is running only once when
     * all the given actions completed.
     *
     * @param actions
     * @param callback the callback to execute once all the results are resolved
     */
    protected final void then(Collection<? extends Action<?>> actions, callback callback) {
    	this.callback = callback;
    	actionRemaining.set(actions.size());
   		canBeExecute = true;
    	if(actionRemaining.get()==0) {//then the callback can be execute
    		this.sendMessage(this, this.actorId, this.actorState);
    	}else {
        	for (Iterator<? extends Action<?>> iterator = actions.iterator(); iterator.hasNext();) {
    			Action<?> action = (Action<?>) iterator.next();
    			action.getResult().subscribe(()->{
    				this.actionRemainingDecrement();});
    		}
    	}
    }

    /**
     * resolve the internal result - should be called by the action derivative
     * once it is done.
     *
     * @param result - the action calculated result
     */
    protected final void complete(R result) {
		this.getResult().resolve(result); //implement choice : we are not warpping this with try&catch to make the flow of the program stop when this error will occur.
		this.actorState.addRecord(this.actionName);
    }
    
    /**
     * @return action's promise (result)
     */
    public final Promise<R> getResult() {
    	return this.Result;
    }
    
    /**
     * send an action to an other actor
     * 
     * @param action
     * 				the action
     * @param actorId
     * 				actor's id
     * @param actorState
	 * 				actor's private state (actor's information)
	 *    
     * @return promise that will hold the result of the sent action
     */
	public Promise<?> sendMessage(Action<?> action, String actorId, PrivateState actorState){
		this.pool.submit(action, actorId, actorState);
		return action.getResult();
	}
	
	/**
	 * set action's name
	 * @param actionName
	 */
	public void setActionName(String actionName){
		this.actionName = actionName;
	}
	
	/**
	 * @return action's name
	 */
	public String getActionName(){
		return this.actionName;
	}
	
	/**
	 * Decrement the current counter for the remaining actions that need to be resolved before calling the callback.
	 */
	protected synchronized void actionRemainingDecrement() {
		this.actionRemaining.decrementAndGet(); //Atomically decrements by one the current value.
		if (this.actionRemaining.intValue()==0) {
			sendMessage(this, this.actorId, this.actorState); // then the callback will be execute
		}
	}
	
	/*
	 * End of File
	 */
}
