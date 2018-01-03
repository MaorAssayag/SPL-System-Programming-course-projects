package bgu.spl181.net.impl.Blockbuster;
import java.util.HashMap;

import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.srv.ConnectionHandler;

public class ConnectionsImpl <T> implements Connections<T> {
	
	private HashMap<Integer, ConnectionHandler<T>> activeClients = new HashMap<Integer, ConnectionHandler<T>>();

	public void UpdateConnections(int connectionId ,ConnectionHandler<T> connectionHandler ){
	    if(!activeClients.containsKey(connectionId))
	        activeClients.put(connectionId,connectionHandler);
    }

	@Override
    public boolean send(int connectionId, T msg) {
        return false;
    }

    @Override
    public void broadcast(T msg) {

    }

    @Override
    public void disconnect(int connectionId) {

    }
    
    public void update() {
		// TODO Auto-generated method stub
	}
 
}
