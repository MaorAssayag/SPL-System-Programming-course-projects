/**
 * 
 */
package bgu.spl.a2.sim.actions;

import java.util.LinkedList;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

/**
 * Assignment doc - Action no.6; will be called from deparment Actor.
 * This action should close a course. Should unregister all the registered students in the
 * course and remove the course from the department courses' list and from the grade sheets of the
 *	students. The number of available spaces of the closed course should be updated to -1. DO NOT
 *	remove its actor. After closing the course, all the request for registration should be denied.
 */
public class closeCourse extends Action<Boolean>{

	String course;
	
	public closeCourse(String course) {
		this.course = course;
		this.Result = new Promise<Boolean>();
		this.setActionName("Close Course");
	}
	
	@Override
	protected void start() {
        LinkedList<Action<Boolean>> temp = new LinkedList<Action<Boolean>>();
        if (((DepartmentPrivateState)this.actorState).getCourseList().contains(this.course)){
            Terminator terminate = new Terminator();
			this.sendMessage(terminate, this.course, (CoursePrivateState)pool.getPrivateState(this.course));
            temp.add(terminate);
        } 
        else { 
        	complete(false);
        	return;
        }
        then(temp, ()->{
            ((DepartmentPrivateState)this.actorState).getCourseList().remove(course);
            complete(true);
        });
	}
	
	/*
	 * End Of File.
	 */
}
