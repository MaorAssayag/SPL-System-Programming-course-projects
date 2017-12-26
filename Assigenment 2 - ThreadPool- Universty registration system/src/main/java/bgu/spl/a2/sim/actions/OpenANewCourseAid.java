package bgu.spl.a2.sim.actions;

import java.util.LinkedList;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

/**
 * 
 * aid class for 'OpenANewClass' action; will be called from the course actor.
 *
 */
public class OpenANewCourseAid extends Action<Boolean>{
	
    private Integer space;
    private LinkedList<String> prequisites;
    
    public OpenANewCourseAid(Integer space, LinkedList<String> prequisites) {
		this.space = space;
		this.prequisites = prequisites;
		this.Result = new Promise<Boolean>();
		this.setActionName("OpenANewCourseAid");
	}
    
	@Override
	protected void start() {
        ((CoursePrivateState)this.actorState).setPrequisites(this.prequisites);
        ((CoursePrivateState)this.actorState).setAvailableSpots(this.space);
        this.complete(true);		
	}

}
