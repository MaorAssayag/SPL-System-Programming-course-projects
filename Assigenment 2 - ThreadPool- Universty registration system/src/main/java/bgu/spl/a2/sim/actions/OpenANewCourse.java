package bgu.spl.a2.sim.actions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

/**
 * Assignment doc - Action no.1; will be called from the department actor.
 * This action opens a new course in a specied department. The course has an initially
 * available spaces and a list of prerequisites.
 */
public class OpenANewCourse extends Action<Boolean> {
	
	private int space;
    private String courseName;
    private LinkedList<String> prequisites;

    public OpenANewCourse(int space , String courseName, LinkedList<String> prequisites){
        this.space = space;
        this.courseName = courseName;
        this.prequisites = prequisites;
        this.Result = new Promise<Boolean>();
        setActionName("Open Course");
    }

    @Override
    protected void start() {
        if (((DepartmentPrivateState)this.actorState).getCourseList().contains(this.courseName)) {
            this.complete(false);
            return;
        }
    	
        OpenANewCourseAid aid = new OpenANewCourseAid(this.space, this.prequisites);
        List<Action<Boolean>> temp = new ArrayList<>();
        temp.add(aid);
        CoursePrivateState courseState = new CoursePrivateState();
        this.sendMessage(aid, this.courseName , courseState); // will register this course in the pool.
    	this.then(temp, ()->{
            if(temp.get(0).getResult().get()){
                ((DepartmentPrivateState)this.actorState).AddCourse(this.courseName);
                this.complete(true);
            }else {
                this.complete(false);
            }	
    	});
    }
	
    /*
     * End Of File.
     */
}
