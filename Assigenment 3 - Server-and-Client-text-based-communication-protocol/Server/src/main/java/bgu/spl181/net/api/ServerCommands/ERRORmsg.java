package bgu.spl181.net.api.ServerCommands;

import bgu.spl181.net.impl.Blockbuster.DataBaseHandler;

/**
 * ERRORmsg - The error command is sent by the server to reply to a failed request.
 * 
 */
public class ERRORmsg extends commandAbstract {

	public ERRORmsg(String mandatoryMSG ) {
		this.MandatoryMessage = mandatoryMSG;
		this.commandName = "ERROR";
	}
	
	@Override
	/**
	 * will return "ERROR <MandatoryMessage>"
	 */
	public String getMsg() {
		return this.commandName + " " + this.MandatoryMessage;
	}

	@Override
	String getType() {
		return this.commandName; //="ERROR"
	}
	
	/**
	 * End of file.
	 */
}
