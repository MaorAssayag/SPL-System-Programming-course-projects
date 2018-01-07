package bgu.spl.a2.sim.actions;

import java.util.LinkedList;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

/**
 * Aid class for closeCourse Action; this action will be called from course Actor.
 * this action will close the course if exist&remove all grades of this course from all the registered students.
 */
public class Terminator extends Action<Boolean>{

	public Terminator() {
		this.Result = new Promise<Boolean>();
		setActionName("Terminate Course");
	}
	
	@Override
	protected void start() {
        ((CoursePrivateState)this.actorState).setAvailableSpots(-1); // by def.
        ((CoursePrivateState)this.actorState).setRegistered(0); // by def.
        LinkedList<Action<Boolean>> temp = new LinkedList<Action<Boolean>>();
        if (((CoursePrivateState)this.actorState).getRegStudents().size() == 0){ // 0 students registered to this course.
            complete(true);
            return;
        }
        for (String student: ((CoursePrivateState)this.actorState).getRegStudents()){
            unRegister unregister = new unRegister(this.actorId, student);
            sendMessage(unregister, this.actorId, this.actorState);
            temp.add(unregister);
        }
        then(temp, ()->{
            complete(true);
        });
	}

	/*
	 * End of File.
	 */
}
