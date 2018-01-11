package bgu.spl181.net.api.ServerCommands;

/**
 * ACKmsg - The acknowledge command is sent by the server to reply
 *          to a successful request by a client.
 *          
 */
public class ACKmsg extends commandAbstract {

	/**
	 * default constructor without optional data.
	 */
	public ACKmsg() {
		this.commandName = "ACK";
	}
	
	/**
	 * constructor with optional data.
	 * @param optionalMsg 
	 */
	public ACKmsg(String optionalMsg) {
		this.commandName = "ACK";
		this.OptionalMessage = optionalMsg;
	}
	
	@Override
	/**
	 * will return "ACK <optional msg if exist>"
	 */
	public String getMsg() {
		return this.commandName + " " + this.OptionalMessage;
	}

	@Override
	String getType() {
		return this.commandName; // ="ACK"
	}
	
	/**
	 * End of file.
	 */
}
