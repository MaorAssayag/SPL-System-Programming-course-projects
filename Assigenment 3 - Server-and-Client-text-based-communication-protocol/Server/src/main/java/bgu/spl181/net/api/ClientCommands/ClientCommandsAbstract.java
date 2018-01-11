package bgu.spl181.net.api.ClientCommands;

import bgu.spl181.net.impl.Blockbuster.DataBaseHandler;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * this interface describe the client Command object.
 * the Command object is used for specify the message to the server by the client.
 * data members :
 *      dataBaseHandler := common data for the lock and path, fix the synchronize issue. 
 *      Commands :=  mandatory data that been sent from the client.
 */
public abstract class ClientCommandsAbstract {
    protected DataBaseHandler dataBaseHandler;
    protected String [] Commands;

    /**
     * This method will change the database of the server if needed. also this method will process the 
     * client-request data and test it if it meets all specific requirements.
     * @return the server response for handling this request i.e : ACK, ERROR etc'.
     */
    public abstract String execute();

    /**
     * End of File.
     */
}
