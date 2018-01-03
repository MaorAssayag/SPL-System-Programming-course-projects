package bgu.spl181.net.impl.Blockbuster;
import bgu.spl181.net.api.bidi.Connections;

public class ConnectionsImpl <T> implements Connections<T> {

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
}
