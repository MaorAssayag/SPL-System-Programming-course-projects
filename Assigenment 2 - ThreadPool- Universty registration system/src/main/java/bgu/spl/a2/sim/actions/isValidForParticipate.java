package bgu.spl.a2.sim.actions;

import java.util.HashMap;
import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

/**
 * Aid class for Participate in course Action; this action will be called from student Actor.
 * This action should try to register the student in the course, if it succeeds, should resolve with true.
 */
public class isValidForParticipate extends Action<Boolean> {

	List<String> prequisites;
	String courseName;
	int grade;
	
	public isValidForParticipate(List<String> prequisites, String courseName, int grade) {
        this.Result = new Promise<Boolean>();
        this.prequisites = prequisites;
        this.courseName = courseName;
        this.grade = grade;
        setActionName("is Valid For Participate");
	}

	@Override
	protected void start() { 
		HashMap<String, Integer>gradeSheet = ((StudentPrivateState)this.actorState).getGrades();
        boolean  isValid = true;
        for(String course : prequisites){
            if(gradeSheet.get(course) == null || gradeSheet.get(course) <= 56){
            	isValid = false;
                break;
            }
        }
        if (isValid) { //update student grade's with this new course !
        	((StudentPrivateState)this.actorState).addGrades(this.courseName,this.grade);
        }
        this.complete(isValid);
	}
	/*
	 * End Of File.
	 */
}
