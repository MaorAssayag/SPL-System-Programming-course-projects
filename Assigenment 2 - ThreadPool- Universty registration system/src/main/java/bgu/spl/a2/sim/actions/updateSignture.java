package bgu.spl.a2.sim.actions;

import java.util.LinkedList;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.Computer;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

/**
 * this class is aid for 'Check obligations' Action; this action will be called from student Actor.
 */
public class updateSignture extends Action<Boolean> {

	Computer computer;
	LinkedList<String> courses;
	
	public updateSignture(Computer computer, LinkedList<String> courses) {
        this.computer = computer;
        this.courses = courses;
        this.Result = new Promise<Boolean>();
        this.setActionName("Update Signture");
	}
	
	@Override
	protected void start() {
        StudentPrivateState studentState = (StudentPrivateState)this.actorState;
        studentState.setSignature(this.computer.checkAndSign(this.courses,studentState.getGrades()));
        this.complete(true);
	}
	/*
	 * End Of File.
	 */
}
