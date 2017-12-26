package bgu.spl.a2.sim;

import bgu.spl.a2.Promise;

/**
 * represents a warehouse that holds a finite amount of computers
 * and their suspended mutexes.
 * releasing and acquiring should be blocking free.
 */
public class Warehouse {
	
	private Computer computers[];
	private int currentCapacity;
	
	public Warehouse(int capacity) {
		this.computers = new Computer[capacity];
		this.currentCapacity = capacity;
	}
	
	/**
	 * add computer to the Warehouse.
	 * @param computer
	 */
	public void addComputer(Computer computer) {
		if (currentCapacity > 0) {
			this.computers[this.computers.length - currentCapacity] = computer;
			currentCapacity--;
		}
	}
	
	/**
	 * 
	 * @param computerType - the id of a computer
	 * @return the requested Computer.
	 */
	public Computer getComputer(String computerType) {
		for(Computer computer : this.computers) {
			if (computer.computerType.equals(computerType)){
				return computer;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param computerType - the ID of a computer
	 * @return the attach SuspendingMutex
	 */
	public SuspendingMutex getSuspendingMutex(String computerType) {
		for(Computer computer : this.computers) {
			if (computer.computerType.equals(computerType)){
				return computer.suspendingMutex;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param computerType - the ID of a computer
	 * @return Promise<Computer> which will be resolve once the computer will be free to use.
	 */
	public Promise<Computer> acquireComputer(String computerType){
		return this.getComputer(computerType).getSuspendingMutex().down();
	}
	
	/**
	 * this function release a computer by its id. 
	 * @param computerType
	 */
	public void releaseComputer(String computerType){
		this.getComputer(computerType).getSuspendingMutex().up();
	}
	/*
	 * End Of File.
	 */
}
