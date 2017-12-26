package bgu.spl.a2.sim.actions;

import java.util.concurrent.CountDownLatch;
import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;


/**
 * this class describe Empty action for the initial attention
 *  to create a slot in the current actor queues.
 *  once the slot has been updated, the initial action that initiate will be resolved.
 */
public class EmptyAction extends Action<Boolean> {

	CountDownLatch currentPhase;
	
	@Override
	protected void start() { 
		currentPhase.countDown();
		this.complete(true);
	}
	
	public EmptyAction(CountDownLatch currentPhase) {
		this.currentPhase = currentPhase;
		this.Result = new Promise<Boolean>();
		this.setActionName("Empty Action");
	}
}
