package bgu.spl.a2;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * an abstract class that represents private states of an actor
 * it holds actions that the actor has executed so far 
 * IMPORTANT: You can not add any field to this class.
 */
public abstract class PrivateState  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> history; // holds the actions' name what were executed

	public List<String> getLogger(){
		return history;
	}
	
	/**
	 * add an action to the records
	 *  
	 * @param actionName
	 */
	public void addRecord(String actionName){
		if (this.history == null) {setHistory(new LinkedList<String>());} // creating new history list
		this.history.add(actionName);
	}
	
	/**
	 * set a new history for the actor action's
	 *  
	 * @param newHistory
	 */
	public void setHistory(List<String> newHistory) {
		this.history = newHistory;
	}
	
	/*
	 * End of File
	 */
}
