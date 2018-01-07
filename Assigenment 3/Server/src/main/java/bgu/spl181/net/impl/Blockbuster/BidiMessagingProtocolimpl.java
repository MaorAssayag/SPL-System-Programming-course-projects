package bgu.spl181.net.impl.Blockbuster;

import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.impl.Blockbuster.gsonimpl.UserMovie;

public class BidiMessagingProtocolimpl<> implements BidiMessagingProtocol<T> {
    private int connectionId;
    private Connections<T> connections;
    private boolean shouldTerminate;
    private DataBaseHandler dataBaseHandler;
    private boolean Login;
    private String Username;

    public BidiMessagingProtocolimpl (DataBaseHandler dataBaseHandler){
        this.dataBaseHandler=dataBaseHandler;
    }
    @Override
    public void start(int connectionId, Connections<T> connections) {
        this.connectionId = connectionId;
        this.connections = connections;
        shouldTerminate = false;
        Username = "";
    }

    @Override
    public String process(String [] message) {
        String ans ="";
        switch (message [0]){
            case "REGISTER":
                if(Username == ""  && !Login){
                    ans = new
                }
        }
    return ans;
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
