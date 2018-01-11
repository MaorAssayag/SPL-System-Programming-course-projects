package bgu.spl181.net.api.ServerCommands;


/**
 * this interface describe the Command object.
 * the Command object is used for specify the message to the client by the server.
 * data members :
 *      commandName := the Command id, i.e: ACK, ERROR etc'
 *      OptionalMessage :=  optional data to be sent with the command
 *      MandatoryMessage := mandatory data to be sent with the command
 */
public abstract class commandAbstract {
	
	protected String OptionalMessage;
	protected String MandatoryMessage;
	protected String commandName;

	/**
	 * 
	 * @return the complete message that sould be sent to the client
	 */
	abstract String getMsg();
	
	/**
	 * 
	 * @return the type of the server response, i.e: ACK,ERROR etc'
	 */
	abstract String getType();
	
	/**
	 * End of file.
	 */
}
