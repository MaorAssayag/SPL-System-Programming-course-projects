package bgu.spl.a2.sim.actions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.Computer;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

/**
 * Assignment doc - Action no.8; this action will be called from department Actor.
 * The department's secretary have to allocate one of the computers available in the warehouse, 
 * and check for each student if he meets some administrative obligations. The computer generates
 * a signature and save it in the private state of the students.
 */
public class CheckObligations extends Action<Boolean> {

	Warehouse warehouse;
	String[] students;
	LinkedList<String> courses;
	String computer;
	Promise<Computer> promise;
	
	public CheckObligations(String[] students, LinkedList<String> courses, Warehouse warehouse, String computer) {	
		this.courses = courses;
		this.computer = computer;
		this.students = students;
		this.warehouse = warehouse;
		this.Result = new Promise<Boolean>();
		this.setActionName("Administrative Check");
	}
	
	@Override
	protected void start() {
		if (this.promise!=null) {
			this.canBeExecute = true;
			 sendMessage(this, this.actorId, this.actorState);
			 return;
		}
        this.promise = this.warehouse.acquireComputer(this.computer);
        promise.subscribe(() -> { // only after we get the computer using promise.
        	this.canBeExecute = true; //for the handle function of this Action (CheckObligations)
            this.callback = ()-> {
                List<Action<Boolean>> temp = new ArrayList<>();
                for (int i = 0; i < this.students.length; i++) {
                    Action<Boolean> updateSignture = new updateSignture(this.promise.get(), this.courses);
                    temp.add(updateSignture);
                    this.sendMessage(updateSignture, this.students[i], (StudentPrivateState)this.pool.getPrivateState(this.students[i]));
				}
                this.then(temp, () -> { 
                    this.warehouse.releaseComputer(this.computer); // will release this computer using up() in his mutex.
                    this.complete(true);
                });
            }; 
            sendMessage(this, this.actorId, this.actorState);//then the pool will handle this call back -> will execute this.call().
        });
	}
	
	/*
	 * End Of File.
	 */
}
