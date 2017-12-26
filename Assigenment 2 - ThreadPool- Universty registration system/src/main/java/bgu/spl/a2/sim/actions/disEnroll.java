package bgu.spl.a2.sim.actions;

import java.util.HashMap;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

/**
 * this is aid class for register/unregister for a cousre; this action will be called for student Actor.
 */
public class disEnroll extends Action<Boolean>{

	String course;
	
	public disEnroll(String course) {
		this.course = course;
		this.Result = new Promise<Boolean>();
		this.setActionName("disEnroll");
	}
	
	@Override
	protected void start() {
        HashMap<String,Integer> gradeSheet = ((StudentPrivateState)this.actorState).getGrades();
        gradeSheet.remove(this.course);
        complete(true);
	}
	/*
	 * End of File.
	 */
}
