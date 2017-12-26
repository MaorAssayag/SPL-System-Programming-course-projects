package bgu.spl.a2.sim.actions;

import java.util.LinkedList;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

/**
 * Assignment doc - Feature; this action will be called from student Actor.
 * This action should try to register the student in the first course in the list, if it succeeds, should add the
 * course to the grades sheet of the student, and give him a grade if supplied. else, it should try againg for the 2nd course etc,.
 */

public class RegisterWithPreferences extends Action<Boolean>{
	
	LinkedList<String> courses;
	LinkedList<Integer> grades;

	public RegisterWithPreferences(LinkedList<String> courses, LinkedList<Integer> grades) {
		this.courses = courses;
		this.grades = grades;
		this.Result = new Promise<Boolean>();
		this.setActionName("Register With Preferences");
	}
	
	@Override
	protected void start() {
		if (this.courses.isEmpty()){
            this.complete(false);
            return;
        }
		if (this.grades.size() < this.courses.size()) { // then the default grade is -1 by definition for each course that missing a grade.
			int i = this.courses.size() - this.grades.size();
			for (int j = 0; j < i; j++) {
				this.grades.add(-1);
			}
		}
        String tryCourse = this.courses.poll();
        if(((CoursePrivateState)this.pool.getPrivateState(tryCourse)).getRegStudents().contains(this.actorId)) {
        	this.sendMessage(this,this.actorId,this.actorState); //try the next course
        	return;
        }

        LinkedList<Action<Boolean>> temp = new LinkedList<>();
        Action<Boolean> tryReg = new ParticipateInCourse(this.actorId,this.grades.poll()); // ParticipateInCourse get student and grade, will be called from the course actor.
        temp.add(tryReg);
        
        this.sendMessage(tryReg, tryCourse , (CoursePrivateState)this.pool.getPrivateState(tryCourse));

        this.then(temp,()->{
            if(temp.poll().getResult().get()) {
                this.complete(true);
            }else {this.sendMessage(this,this.actorId,this.actorState);}//try again
        });
		
	}

}
