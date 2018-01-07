package bgu.spl.a2.sim.privateStates;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import bgu.spl.a2.PrivateState;

/**
 * this class describe course's private state
 */
public class CoursePrivateState extends PrivateState implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer availableSpots;
	private Integer registered;
	private List<String> regStudents;
	private List<String> prequisites;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 */
	public CoursePrivateState() {
		regStudents = new LinkedList<String>();
		prequisites = new LinkedList<String>();
		availableSpots = 0;
		registered = 0;
		this.setHistory(new LinkedList<String>());	
	}

	public Integer getAvailableSpots() {
		return availableSpots;
	}

	public Integer getRegistered() {
		return registered;
	}

	public List<String> getRegStudents() {
		return regStudents;
	}

	public List<String> getPrequisites() {
		return prequisites;
	}
	
	public void setAvailableSpots(Integer availableSpots) {
		this.availableSpots = availableSpots;
	}

	public void setPrequisites(List<String> prequisites) {
		this.prequisites = prequisites;
	}
	
	public void addPrequisite(String prequisite) {
		this.prequisites.add(prequisite);
	}


	public void setRegistered(Integer registered) {
		this.registered = registered;
	}

	public void addRegStudent(String regStudents) {
		if(!this.regStudents.contains(regStudents) && this.availableSpots.intValue() > 0) {
			this.regStudents.add(regStudents);
			this.setRegistered(this.registered.intValue()+1);
			this.setAvailableSpots(this.availableSpots.intValue()-1);
		}
	}
	
	public void RemoveStudent (String studentName){
		if(this.regStudents.remove(studentName)) // return true if the list changed (:= the element was in the list)
			this.setRegistered(this.registered.intValue()-1);
			this.setAvailableSpots(this.availableSpots.intValue()+1);
	}
	
	/*
	 * EndOfFile
	 */
}
