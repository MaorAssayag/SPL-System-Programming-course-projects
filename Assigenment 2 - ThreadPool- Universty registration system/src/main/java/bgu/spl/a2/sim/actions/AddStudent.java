package bgu.spl.a2.sim.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;


/**
 * Assignment doc - Action no.2;
 * this class describe "add student" action.
 */
public class AddStudent extends Action<Boolean> {
    private String studentID;
    private CountDownLatch currentPhase;

    public AddStudent(String studentID,CountDownLatch currentPhase){
        this.setActionName("Add Student");
        this.studentID = studentID;
        this.Result = new Promise<Boolean>();
        this.currentPhase = currentPhase;
    }

    @Override
    protected void start() {
    	if (((DepartmentPrivateState)this.actorState).getStudentList().contains(studentID)) {
    		this.complete(false);
    		return;
    	}
    	List<Action<Boolean>> temp = new ArrayList<>();
    	EmptyAction aid2 = new EmptyAction(new CountDownLatch(1));
    	this.sendMessage(aid2, this.studentID, new StudentPrivateState());
    	temp.add(aid2);
    	
    	this.then(temp, ()->{ // after the student was added to the pool
        	if(temp.get(0).getResult().get() && ((DepartmentPrivateState)this.actorState).AddStudent(studentID)) { // true if there was a change in the list
        		this.complete(true);
         	}else { this.complete(false);}
        	this.currentPhase.countDown();
    	});
    	
    }
    /*
     * End Of File.
     */
}