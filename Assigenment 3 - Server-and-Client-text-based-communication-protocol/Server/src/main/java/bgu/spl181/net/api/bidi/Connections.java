package bgu.spl181.net.api.bidi;

import java.io.IOException;

/**
 * 
 * this interface will allowed the protocol to send the client message via his Connection handler.
 *
 * @param <T>
 */
public interface Connections<T> {

	/**
	 * will send <T> message to the client from the connection Handler.
	 * 
	 * this method will be called by the protocol.
	 * @param connectionId - the id of the current client.
	 * @param msg
	 * @return boolean if the server was able to send the message successfully.
	 */
    boolean send(int connectionId, T msg);

    /**
     * will send <T> message to the client from the connection Handler.
     * @param msg
     */
    void broadcast(T msg);
    
    /**
     * will remove this client from the database that contaion the current active clients.
     * @param connectionId
     */
    void disconnect(int connectionId);
    
    /**
     * End of File.
     */
}
