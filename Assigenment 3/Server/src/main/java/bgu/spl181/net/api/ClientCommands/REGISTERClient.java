package bgu.spl181.net.api.ClientCommands;

import bgu.spl181.net.api.commands.ACKmsg;
import bgu.spl181.net.api.commands.ERRORmsg;
import bgu.spl181.net.impl.Blockbuster.DataBaseHandler;
import bgu.spl181.net.impl.Blockbuster.gsonimpl.UserJson;
import bgu.spl181.net.impl.Blockbuster.gsonimpl.user;
import bgu.spl181.net.impl.Blockbuster.gsonimpl.users;

/**
 * this class represent an implementaion of the client command process levels.
 * Client commands:
 * 
 *	1) REGISTER <username> <password> [Data block,…] Used to register a new user to the system.
 *		• Username – The user name.
 *		• Password – the password.
 *		• Data Block – An optional block of additional information that may be used by the service.
 *
 * In case of failure, an ERROR command will be sent by the server: ERROR registration failed Reasons for failure:
 *		1. The client performing the register call is already logged in.
 *		2. The username requested is already registered in the system.
 *		3. Missing info (username/password).
 *		4. Data block does not fit service requirements (defined in rental service section).
 *
 * In case of successful registration an ACK command will be sent: ACK registration succeeded
 *
 */
public class REGISTERClient extends ClientCommandsAbstract {

   public REGISTERClient(DataBaseHandler dataBaseHandler , String [] Commands){
       this.dataBaseHandler = dataBaseHandler;
       this.Commands = Commands;
   }

   /**
    * REGISTER <username> <password> [Data block,…] Used to register a new user to the system.
    *		• Username – The user name.
    *		• Password – the password.
    *		• Data Block – An optional block of additional information that may be used by the service.
    *
    *
    * When a REGISTER command is processed the user created will be a normal user with credit balance 0 by default.
	* The service requires additional information about the user and the data block is where the user inserts that information.
	* In this case, the only information we save on a specific user that is recieved from the REGISTER command is the users origin country.
	* REGISTER <username> <password> country=”<country name>”
    */
    @Override
    public String execute() {
        String type = "normal";
        String country = "";
        String balance ="0";
        //check the optional data block
        for (int i = 3; i < Commands.length; i++) {
            if(Commands[i].contains("type")){
                type = Commands[i].substring(Commands[i].indexOf('"'),Commands[i].lastIndexOf('"'));
            }
            if(Commands[i].contains("country")){
                country = Commands[i].substring(Commands[i].indexOf('"'),Commands[i].lastIndexOf('"'));
            }
            if(Commands[i].contains("balance")){
                balance = Commands[i].substring(Commands[i].indexOf('"'),Commands[i].lastIndexOf('"'));
            }
        }
        if(Commands.length >= 3 && !Commands[1].isEmpty() && !Commands[2].isEmpty() ) {
            dataBaseHandler.getReadWriteLockUsers().writeLock().lock();//lock the Users json file
            UserJson temp = new UserJson(dataBaseHandler.getPathUsers());
            users users = temp.getUsers();
            if(!users.adduser(new user(Commands[1], Commands[2], type, country, balance))){
                temp.UpdateUser(users);// write to the json
                dataBaseHandler.getReadWriteLockUsers().writeLock().unlock();
                return new ACKmsg("registration succeeded").getMsg();
            }else {
                dataBaseHandler.getReadWriteLockUsers().writeLock().unlock();
                return (new ERRORmsg("registration failed")).getMsg();
            }
        }else
            return (new ERRORmsg("registration failed")).getMsg();
   }
    
    /**
     * End Of File.
     */
}