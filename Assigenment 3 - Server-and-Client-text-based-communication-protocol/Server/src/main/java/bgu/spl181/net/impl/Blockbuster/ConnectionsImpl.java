package bgu.spl181.net.impl.Blockbuster;
import java.util.HashMap;
import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.srv.ConnectionHandler;

/**
 * This class is an implementation of Connections which allow to send 
 * the active clients the server response via his handler to the handler by a thread from the ThreadPool. 
 * 
 * @param <T>
 */
public class ConnectionsImpl <T> implements Connections<T> {
	
	private HashMap<Integer, ConnectionHandler<T>> activeClients = new HashMap<Integer, ConnectionHandler<T>>();
	int counter = -1; // the first ID will be 0.
	
	/**
	 * add to the current database of the active clients a new client with a unique clientID and his handler.
	 * @param connectionHandler
	 * @return the new client ID.
	 */
	public int addConnection(ConnectionHandler<T> connectionHandler){
		counter++;
	   activeClients.put(counter,connectionHandler);
	   return counter;
    }
	
	@Override
    public boolean send(int connectionId, T msg) {
		if (!this.activeClients.containsKey(connectionId))
			return false; // this client id doesnt exist
		try {
			this.activeClients.get(connectionId).send(msg);
			return true; // the msg was send successfully 
		} 
		catch (Exception e) {
			return false; // the msg wasn't sent
		}
    }

    @Override
    public void broadcast(T msg) {
    	for(int id:this.activeClients.keySet()) {
    		((ConnectionHandler<T>)this.activeClients.get(id)).getProtocol().process(msg);
    	}
    }

    @Override
    public void disconnect(int connectionId) {
    	if(this.activeClients.containsKey(connectionId)) {
    		this.activeClients.remove(connectionId); // remove this client from the hash map
    	}
    } 
    
    /**
     * End of File.
     */
}
