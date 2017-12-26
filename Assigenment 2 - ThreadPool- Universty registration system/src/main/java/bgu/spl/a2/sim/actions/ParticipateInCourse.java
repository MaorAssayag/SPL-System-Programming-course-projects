package bgu.spl.a2.sim.actions;

import java.util.LinkedList;
import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

/**
 * Assignment doc - Action no.3; this action will be called from course Actor.
 * This action should try to register the student in the course, if it succeeds, should add the
 * course to the grades sheet of the student, and give him a grade if supplied. See the input example.
 */
public class ParticipateInCourse extends Action<Boolean> {
    private int grade;
    private String studentID;
    

	public ParticipateInCourse(String studentID, int grade) {
		this.grade = grade;
		this.studentID = studentID;
		this.Result = new Promise<Boolean>();
		this.setActionName("Participate In Course");
	}
	
	public String getStudentId() {
		return this.studentID;
	}
	
	@Override
	protected void start() {
        StudentPrivateState studentState = (StudentPrivateState)this.pool.getPrivateState(this.studentID);
        if (studentState == null) { // the student isn't in the system.
        	this.complete(false);
        	return;
        }
        
		if(((CoursePrivateState)this.actorState).getRegStudents().contains(this.studentID)) {
			this.sendMessage(this, this.actorId, this.actorState); //try again later
			return;
		}
		
        if (((CoursePrivateState)actorState).getAvailableSpots() == 0){ //there is no available space in this course.
        	this.complete(false);
            return;
        }
        
        List<String> prequisites = ((CoursePrivateState)actorState).getPrequisites();
        Action<Boolean> isValid = new isValidForParticipate(prequisites,this.actorId,grade);
        List<Action<Boolean>> temp = new LinkedList<Action<Boolean>>();
        temp.add(isValid);//list that contains isValid
        @SuppressWarnings("unchecked") // for elimination of a warning.
		Promise<Boolean> result = (Promise<Boolean>)this.sendMessage(isValid,this.studentID,studentState); // return the promise of isValid&insert isValid to the pool.
        
        then(temp,()->{ //still in the course Actor
            if (result.get().booleanValue()) { // then the student registration is valid.
            	if (((CoursePrivateState)this.actorState).getAvailableSpots().intValue() == 0) {
                    Action<Boolean> disenroll = new disEnroll(this.actorId); //remove the grade of this course from the student
                    sendMessage(disenroll, this.studentID, studentState);
                    disenroll.getResult().subscribe(()->this.complete(false));
            	}
            	else{
            		((CoursePrivateState)this.actorState).addRegStudent(this.studentID);
            		this.complete(true);
            	}
            }
            else {this.complete(false); }//for later if necessary tracking.
        });        
	}
	
	/*
	 * End Of File.
	 */
}
