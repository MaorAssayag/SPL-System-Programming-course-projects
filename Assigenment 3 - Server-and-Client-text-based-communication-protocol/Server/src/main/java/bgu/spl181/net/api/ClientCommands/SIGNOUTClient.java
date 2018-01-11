package bgu.spl181.net.api.ClientCommands;

import bgu.spl181.net.api.ServerCommands.ACKmsg;
import bgu.spl181.net.impl.Blockbuster.DataBaseHandler;

/**
 * this class represent an implementaion of the client LOGIN command process levels.
 * 
 * Sign out from the server.
	In case of failure, an ERROR command will be sent by the server: 
	ERROR signout failed Reasons for failure:
		1. Client not logged in.
	In case of successful sign out an ACK command will be sent: ACK signout
	succeeded After a successful ACK for sign out the client should terminate!
 *
 */
public class SIGNOUTClient extends ClientCommandsAbstract {

	/**
	 * default constructor.
	 * @param dataBaseHandler
	 * @param Commands
	 */
	public SIGNOUTClient(DataBaseHandler dataBaseHandler , String[] Commands) {
	       this.dataBaseHandler = dataBaseHandler;
	       this.Commands = Commands;
	}
	/**
	 * execute the SIGNOUT command request from the client.
	 * 
	 * Commands array should contain : {"SIGNOUT"}
	 */
	@Override
	public String execute() {
		return new ACKmsg("login succeeded").getMsg();
	}

	/**
	 * End of File.
	 */
}
