package bgu.spl181.net.api.ClientCommands;

import bgu.spl181.net.api.ServerCommands.ACKmsg;
import bgu.spl181.net.api.ServerCommands.ERRORmsg;
import bgu.spl181.net.impl.Blockbuster.DataBaseHandler;
import bgu.spl181.net.impl.Blockbuster.gsonimpl.UserJson;
import bgu.spl181.net.impl.Blockbuster.gsonimpl.user;
import bgu.spl181.net.impl.Blockbuster.gsonimpl.users;

/**
 * this class represent an implementaion of the client LOGIN command process levels.
 * 2) LOGIN <username> <password> - Used to login into the system.
 *		• Username – The username.
 *		• Password – The password.
 *
 * In case of failure, an ERROR command will be sent by the server: ERROR login failed Reasons for failure:
 *		1. Client performing LOGIN command already performed successful LOGIN command.
 *		2. Username already logged in.
 *		3. Username and Password combination does not fit any user in the system.
 *
 *	In case of a successful login an ACK command will be sent: ACK login succeeded
 */
public class LOGINClient extends ClientCommandsAbstract {
	
	/**
	 * default constructor.
	 * @param dataBaseHandler
	 * @param Commands
	 */
	public LOGINClient(DataBaseHandler dataBaseHandler , String[] Commands) {
	       this.dataBaseHandler = dataBaseHandler;
	       this.Commands = Commands;
	}
	
	/**
	 * execute the LOGIN command request from the client.
	 * 
	 * Commands array should contain : {"LOGIN",<username>,<password>}
	 */
	@Override
	public String execute() {
        //login the user if fit the requirements
        if(Commands.length >= 3 && !Commands[1].isEmpty() && !Commands[2].isEmpty()) {
            dataBaseHandler.getReadWriteLockUsers().readLock().lock();//lock the Users json file
            UserJson temp = new UserJson(dataBaseHandler.getPathUsers());
            users users = temp.getUsers();
            if (users.exist(Commands[1], Commands[2])) {
            	dataBaseHandler.getReadWriteLockUsers().readLock().unlock();
            	return new ACKmsg("login succeeded").getMsg();
            }
            dataBaseHandler.getReadWriteLockUsers().readLock().unlock();
        }
        return (new ERRORmsg("login failed")).getMsg();
	}

    /**
     * End of File.
     */
}
