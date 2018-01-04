package bgu.spl181.net.impl.Blockbuster;

import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.api.bidi.Connections;

public class BidiMessagingProtocolimpl<T> implements BidiMessagingProtocol<T> {
    private int connectionId;
    private Connections<T> connections;
    boolean shouldTerminate;



    @Override
    public void start(int connectionId, Connections<T> connections) {
        this.connectionId = connectionId;
        this.connections = connections;
        shouldTerminate = false;
    }

    @Override
    public T process(T message) {
        return null;
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
