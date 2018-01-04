package bgu.spl181.net.api.commands;

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
	String getMsg() {
		return this.commandName + " " + this.MandatoryMessage;
	}
	
	/**
	 * End of file.
	 */
}
