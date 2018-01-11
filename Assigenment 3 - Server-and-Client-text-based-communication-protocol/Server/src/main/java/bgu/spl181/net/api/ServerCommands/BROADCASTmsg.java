package bgu.spl181.net.api.ServerCommands;

import bgu.spl181.net.impl.Blockbuster.DataBaseHandler;

/**
 * BROADCASTmsg - The broadcast command is sent by the server to all logged in clients
 * 
 */
public class BROADCASTmsg extends commandAbstract {

	/**
	 * constructor with mandatory data.
	 * @param mandatoryMSG
	 */
	public BROADCASTmsg(String mandatoryMSG) {
		this.MandatoryMessage = mandatoryMSG;
		this.commandName = "BROADCAST";
	}
	
	@Override
	/**
	 * will return "BROADCAST <MandatoryMessage>"
	 */
	public String getMsg() {
		return this.commandName + " " + this.MandatoryMessage;
	}

	@Override
	public String getType() {
		return this.commandName; //="BROADCAST"
	}
	
	/**
	 * End of file.
	 */
}
