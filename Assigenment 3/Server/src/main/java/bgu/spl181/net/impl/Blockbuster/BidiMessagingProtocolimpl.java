package bgu.spl181.net.impl.Blockbuster;

import bgu.spl181.net.api.ClientCommands.REGISTERClient;
import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.api.commands.ERRORmsg;
import bgu.spl181.net.impl.Blockbuster.gsonimpl.UserMovie;
import bgu.spl181.net.srv.ConnectionHandler;

public class BidiMessagingProtocolimpl implements BidiMessagingProtocol<String> {
    private int connectionId;
    private Connections<String> connections;
    private boolean shouldTerminate;
    private DataBaseHandler dataBaseHandler;
    private boolean Login;
    private String Username;

    public BidiMessagingProtocolimpl (DataBaseHandler dataBaseHandler){
        this.dataBaseHandler=dataBaseHandler;
    }
    @Override
    public void start(int connectionId, Connections<String> connections) {
        this.connectionId = connectionId;
        this.connections = connections;
        shouldTerminate = false;
        Username = "";
    }

    @Override
    public String process(String messagein) {
        String [] message =stringToArray(messagein);
        String ans ="";
        switch (message [0]){
            case "REGISTER":
                if(Username == ""  && !Login){
                    ans = new REGISTERClient(dataBaseHandler,message).execute();
                }else
                    ans = new ERRORmsg("registration failed").getMsg();
        }
    return ans;
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
    private String[] stringToArray(String msg) {
        String[] result = msg.split(" ");
        return result;
    }
}
