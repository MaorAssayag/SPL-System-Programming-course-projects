package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

/**
* Assignment doc - Action no.2; this action will be called from course Actor.
* This action should try to add space for students in the course, if it succeeds, should update the available spots.
*/
public class addSpace extends Action<Boolean> {

	String course;
	int numToAdd;
	
	public addSpace(String course, int num) {
		this.course = course;
		this.numToAdd = num;
		this.Result = new Promise<Boolean>();
		this.setActionName("Add Spaces");
	}
	
	@Override
	protected void start() {
       this.actorState.addRecord(getActionName());
       Integer currentSpot = ((CoursePrivateState)this.actorState).getAvailableSpots();
       if(currentSpot.intValue() != -1) {//then isnt close
           ((CoursePrivateState)this.actorState).setAvailableSpots(currentSpot + (Integer)this.numToAdd);
           this.complete(true);
       }else {
    	   this.complete(false);
       }
	}

	/*
	 * End Of File.
	 */
}
