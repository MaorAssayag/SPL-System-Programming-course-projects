package bgu.spl181.net.api.ClientCommands;

/**
 * this class represent an implementaion of the client LOGIN command process levels.
 * 2) LOGIN <username> <password> - Used to login into the system.
 *		• Username – The username.
 *		• Password – The password.
 *
 *	In case of failure, an ERROR command will be sent by the server: ERROR login failed Reasons for failure:
 *		1. Client performing LOGIN command already performed successful LOGIN command.
 *		2. Username already logged in.
 *		3. Username and Password combination does not fit any user in the system.
 *
 *	In case of a successful login an ACK command will be sent: ACK login succeeded
 */
public class LOGINClient extends ClientCommandsAbstract {

	@Override
	public String execute() {
		// TODO Auto-generated method stub
		return null;
	}

}
