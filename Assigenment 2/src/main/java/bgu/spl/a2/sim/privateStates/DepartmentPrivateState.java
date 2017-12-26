package bgu.spl.a2.sim.privateStates;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import bgu.spl.a2.PrivateState;

/**
 * this class describe department's private state
 */
public class DepartmentPrivateState extends PrivateState implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> courseList;
	private List<String> studentList;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 */
	public DepartmentPrivateState() {
		this.courseList = new LinkedList<String>();
		this.studentList = new LinkedList<String>();
		this.setHistory(new LinkedList<String>());
	}
	
	public boolean AddCourse(String course){
		return this.courseList.add(course); //true if this list changed as a result of the call
	}
	
	public boolean AddStudent(String student){
		return this.studentList.add(student); //true if this list changed as a result of the call
	}
	
	public void removeCours(String course){
		this.courseList.remove(course);
	}
	
	public void removeStudent(String student){
		this.studentList.remove(student);
	}

	public List<String> getCourseList() {
		return this.courseList;
	}

	public List<String> getStudentList() {
		return this.studentList;
	}
	
	/*
	 * EndOfFile
	 */
}
