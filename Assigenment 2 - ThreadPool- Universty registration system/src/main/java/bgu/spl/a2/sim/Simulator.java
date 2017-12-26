/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.sim.actions.AddStudent;
import bgu.spl.a2.sim.actions.CheckObligations;
import bgu.spl.a2.sim.actions.EmptyAction;
import bgu.spl.a2.sim.actions.OpenANewCourse;
import bgu.spl.a2.sim.actions.ParticipateInCourse;
import bgu.spl.a2.sim.actions.RegisterWithPreferences;
import bgu.spl.a2.sim.actions.addSpace;
import bgu.spl.a2.sim.actions.closeCourse;
import bgu.spl.a2.sim.actions.unRegister;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {

	public static ActorThreadPool actorThreadPool;
	public static JsonObject currentJsonObject;
	public static Warehouse warehouse;
	
	
	/**
	* Begin the simulation Should not be called before attachActorThreadPool()
	*/
    public static void start(){
    	//0. get the computers array and add them to a new warehouse.
    	//
    	JsonArray computers = currentJsonObject.getAsJsonArray("Computers");
    	warehouse = new Warehouse(computers.size());
    	for (int i = 0; i < computers.size(); i++) {
			JsonObject data = computers.get(i).getAsJsonObject();
			Computer currentComputer = new Computer(data.get("Type").getAsString());
			currentComputer.setFailSig(data.get("Sig Fail").getAsLong());
			currentComputer.setSuccessSig(data.get("Sig Success").getAsLong());
			warehouse.addComputer(currentComputer);
		}
    	System.out.println("Phase 0 finished.");
    	
    	//1. Phase 1 - An array of all the open courses actions, and some other action might appear. All the actions
    	// in Phase 1 should be completed before proceeding to Phase 2.
    	actorThreadPool.start();
    	JsonArray actions = currentJsonObject.getAsJsonArray("Phase 1");
    	CountDownLatch phase1 = new CountDownLatch(actions.size());
    	for (int i = 0; i < actions.size(); i++) {
			JsonObject data = actions.get(i).getAsJsonObject();
			addAction(data , phase1); // each action will count down phase1 when it is complete.
    	}
    	try {phase1.await();}
    	catch (InterruptedException e) {} // phase 1 is done.
    	System.out.println("Phase 1 finished.");
    	
    	//2. Phase 2 - An array of all the open courses actions, and some other action might appear. All the actions
    	//  in Phase 2 should be completed before proceeding to Phase 3.
    	actions = currentJsonObject.getAsJsonArray("Phase 2");
    	CountDownLatch phase2 = new CountDownLatch(actions.size());
    	for (int i = 0; i < actions.size(); i++) {
			JsonObject data = actions.get(i).getAsJsonObject();
			addAction(data , phase2); // each action will count down phase2 when it is complete.
    	}
    	try {phase2.await();}
    	catch (InterruptedException e) {} // phase 2 is done.
    	System.out.println("Phase 2 finished.");
    	
    	
    	//3. Phase 3 - An array of all the open courses actions, and some other action might appear. All the actions
    	// in Phase 3 should be completed before proceeding to Phase 3.
    	actions = currentJsonObject.getAsJsonArray("Phase 3");
    	CountDownLatch phase3 = new CountDownLatch(actions.size()); 	
    	for (int i = 0; i < actions.size(); i++) {
			JsonObject data = actions.get(i).getAsJsonObject();
			addAction(data ,phase3); // phase3 countDownLatch currently not in use.
    	} 
    	try {phase3.await();}
    	catch (InterruptedException e) {} // phase 3 is done.
    	System.out.println("Phase 3 finished.");
    	
    	//4. end the simulator.
    	end(); // end simulator.
    }
    
	
	/**
	* attach an ActorThreadPool to the Simulator, this ActorThreadPool will be used to run the simulation
	* 
	* @param myActorThreadPool - the ActorThreadPool which will be used by the simulator
	*/
	public static void attachActorThreadPool(ActorThreadPool myActorThreadPool){
		actorThreadPool = myActorThreadPool;
	}
	
	/**
	* shut down the simulation
	* returns list of private states
	*/
	public static HashMap<String,PrivateState> end(){
		HashMap<String,PrivateState> result = actorThreadPool.getActorsHash();
		try(ObjectOutputStream objectOutput = new ObjectOutputStream(new FileOutputStream("result.ser"));) {
			actorThreadPool.shutdown();
			objectOutput.writeObject(result);
		}
		catch(FileNotFoundException e) {System.out.println("C'ant find the file ! ");}
		catch(Exception ex) {System.out.println("problem found" + ex);}
		System.out.println("simulator finished !");
		return result;
	}
	
	/**
	* main method.
	*
	*/
	public static void main(String[] args){
		BufferedReader bufferedReader = null;
		try { bufferedReader = new BufferedReader(new FileReader(args[0]));} // WARNING : need to check this
		catch (FileNotFoundException e) { System.out.println("File Not Found at:" + args[0]); }
		currentJsonObject = new Gson().fromJson(bufferedReader, JsonObject.class);
		
		int threadsNum = currentJsonObject.get("threads").getAsInt(); //threadsNum:=how many threads to run.
		attachActorThreadPool(new ActorThreadPool(threadsNum));
		start();
	}
	
    /**
     * addAction - get the action data in JsonObject & submit it to actorThreadPoll according to its type.
     */
    public static void addAction(JsonObject currentAction, CountDownLatch currentPhase) {
    	String actionType = currentAction.get("Action").getAsString();
    	switch (actionType) {
    		case "Open Course":{
    			String department = currentAction.get("Department").getAsString();
    			String course = currentAction.get("Course").getAsString();
    			int space = currentAction.get("Space").getAsInt();
                LinkedList<String> prerequisites = new LinkedList<>();
                Iterator<JsonElement> it = currentAction.get("Prerequisites").getAsJsonArray().iterator();
                while (it.hasNext())
                	prerequisites.add(it.next().getAsString()); // add each prerequisite
                //department State handle
                DepartmentPrivateState departmentState; 
				if (!actorThreadPool.getActors().containsKey(department)){
					departmentState = new DepartmentPrivateState();
					CountDownLatch tempForAction = new CountDownLatch(1);
					actorThreadPool.submit(new EmptyAction(tempForAction), department, departmentState);
			    	try {tempForAction.await();}
			    	catch (InterruptedException e) {} // department has been added.
				}else{
					departmentState = (DepartmentPrivateState)actorThreadPool.getPrivateState(department);
				}
                OpenANewCourse open = new OpenANewCourse(space, course, prerequisites);
                actorThreadPool.submit(open, department, departmentState);  //will call the handle function for 'open'
                open.getResult().subscribe(() -> {currentPhase.countDown();});
    		}
    		break;
    		
    		case "Add Student":{
    			String department = currentAction.get("Department").getAsString();
    			String studentID = currentAction.get("Student").getAsString();

				AddStudent add = new AddStudent(studentID,currentPhase);
				if (!actorThreadPool.getActors().containsKey(department)){
					actorThreadPool.submit(add, department, new DepartmentPrivateState());
				}else{
					actorThreadPool.submit(add, department, (DepartmentPrivateState)actorThreadPool.getPrivateState(department));
				}
    		}
    		break;
    		
    		case "Participate In Course":{
    			String studentID = currentAction.get("Student").getAsString();
    			String course = currentAction.get("Course").getAsString();
    			int grade = currentAction.get("Grade").getAsJsonArray().get(0).getAsInt();
				if (!actorThreadPool.getActors().containsKey(course)){
					currentPhase.countDown();
					return;}//there is no such course in the system
				ParticipateInCourse praticipate = new ParticipateInCourse(studentID, grade);
				actorThreadPool.submit(praticipate, course, (CoursePrivateState)actorThreadPool.getPrivateState(course)); 
				praticipate.getResult().subscribe(() -> {currentPhase.countDown();});
    		}
    		break;
    		
    		case "Add Spaces":{
    			int num = currentAction.get("Number").getAsInt();
    			String course = currentAction.get("Course").getAsString();
    			if (!actorThreadPool.getActors().containsKey(course)){
    				currentPhase.countDown();
					return;}//there is no such course in the system
    			addSpace addspace = new addSpace(course, num);
    			actorThreadPool.submit(addspace, course, actorThreadPool.getPrivateState(course));
    			addspace.getResult().subscribe(() -> {currentPhase.countDown();});
    		}
    		break;
    		
    		case "Register With Preferences":{
                String studentID = currentAction.get("Student").getAsString();
                LinkedList<String> preferences = new LinkedList<>();
                Iterator<JsonElement> it = currentAction.get("Preferences").getAsJsonArray().iterator();
                while (it.hasNext())
                    preferences.add(it.next().getAsString());

                LinkedList<Integer> grades = new LinkedList<>();
                it = currentAction.get("Grade").getAsJsonArray().iterator();
                while (it.hasNext())
                    grades.add(it.next().getAsInt());
                
                RegisterWithPreferences regWithPre = new RegisterWithPreferences(preferences,grades);
                actorThreadPool.submit(regWithPre, studentID, actorThreadPool.getPrivateState(studentID));
                regWithPre.getResult().subscribe(() -> {currentPhase.countDown();});
    		}
    		break;
    		
    		case "Unregister":{
    			String studentID = currentAction.get("Student").getAsString();
    			String course = currentAction.get("Course").getAsString();
    			if (!actorThreadPool.getActors().containsKey(course)){
    				currentPhase.countDown();
					return; //there is no such course in the system
				}
    			unRegister un = new unRegister(course, studentID);
    			actorThreadPool.submit(un, course, actorThreadPool.getPrivateState(course));
    			un.getResult().subscribe(() -> {currentPhase.countDown();});
    		}
    		break;
    		
    		case "Close Course":{
				String department = currentAction.get("Department").getAsString();
				if (!actorThreadPool.getActors().containsKey(department)){
					currentPhase.countDown();
					return; //there is no such department in the system
				}
				String course = currentAction.get("Course").getAsString();
				if (!actorThreadPool.getActors().containsKey(course)){
					currentPhase.countDown();
					System.out.println("count down -1 from  :" + currentPhase.getCount());
					return; //there is no such course in the system
				}
				closeCourse close = new closeCourse(course);
				actorThreadPool.submit(close, department, actorThreadPool.getPrivateState(department));
				close.getResult().subscribe(() -> {currentPhase.countDown();});
    		}
    		break;  
    		
    		case "Administrative Check":{
    			String department = currentAction.get("Department").getAsString();
				if (!actorThreadPool.getActors().containsKey(department)){
					currentPhase.countDown();
					return; //there is no such department in the system
				}
				String computer = currentAction.get("Computer").getAsString();
		    	JsonArray studentsJsonArray = currentAction.getAsJsonArray("Students");
				String[] studentsArray = new String[studentsJsonArray.size()];
				for (int i = 0; i < studentsArray.length; i++) {
					studentsArray[i] = studentsJsonArray.get(i).getAsString();
				}
				JsonArray coursesJsonArray = currentAction.getAsJsonArray("Conditions");
				LinkedList<String> coursesList = new LinkedList<String>();
				for (int i = 0; i < coursesJsonArray.size(); i++) {
					coursesList.add(coursesJsonArray.get(i).getAsString());
				}
				CheckObligations checkObligations = new CheckObligations(studentsArray, coursesList, warehouse, computer);
				actorThreadPool.submit(checkObligations, department, actorThreadPool.getPrivateState(department));
				checkObligations.getResult().subscribe(() -> {currentPhase.countDown();});
    		}
    		break;	
    	}
    }
	
	/*
	 * End Of File.
	 */
	
}
