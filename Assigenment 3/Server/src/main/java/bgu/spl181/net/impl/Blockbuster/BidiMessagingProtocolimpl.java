package bgu.spl181.net.impl.Blockbuster;

import java.util.concurrent.atomic.AtomicBoolean;

import bgu.spl181.net.api.ClientCommands.ClientCommandsAbstract;
import bgu.spl181.net.api.ClientCommands.LOGINClient;
import bgu.spl181.net.api.ClientCommands.REGISTERClient;
import bgu.spl181.net.api.ClientCommands.SIGNOUTClient;
import bgu.spl181.net.api.ServerCommands.ERRORmsg;
import bgu.spl181.net.api.ServerCommands.commandAbstract;
import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.impl.Blockbuster.gsonimpl.UserMovie;
import bgu.spl181.net.srv.ConnectionHandler;

/**
 * This class is an implementation of BidiMessaginigProtocol which allow to process 
 * the client request via his handler by a thread from the ThreadPool. 
 * data members :
 * 		connectionId := the unique integer given by the ConnectionImpl class when we create a new handler for client's.
 * 		connections := for sending a message back to the client.
 * 		shouldTerminate := when we get logout command, the client handling should be terminate.
 * 		login := boolean that indicate if the user request to log in the system before.
 * 		username := the name of this client.
 *
 */
public class BidiMessagingProtocolimpl implements BidiMessagingProtocol<String> {
    private int connectionId;
    private Connections<String> connections;
    private boolean shouldTerminate;
    private DataBaseHandler dataBaseHandler;
    private AtomicBoolean login;
    private String username = "";

    /**
     * default constructor.
     * @param dataBaseHandler
     */
    public BidiMessagingProtocolimpl (DataBaseHandler dataBaseHandler){
        this.dataBaseHandler = dataBaseHandler;
    }
    
    /**
     * Initialize the client handler with his data - according to the Reactor design-pattern.
     */
    @Override
    public void start(int connectionId, Connections<String> connections) {
        this.connectionId = connectionId;
        this.connections = connections;
        shouldTerminate = false;
    }

    /**
     * The main function that handle the client. this method will be called from the
     * (NonBlicking/Blocking)ConnectionHandler. 
     * this String will be converted to array of string that contains in each cell a specific piece of data. 
     * @param messagein : this will be the client orginal stream to the server.
     * @return  the respone of the server to the client, that should be send to the client via the channel.
     */
    @Override
    public String process(String messagein) {
        String [] message = stringToArray(messagein);
        String ans = "";
        switch (message [0]){
            case "REGISTER":
                if(username == ""  && !login.get())
                    ans = new REGISTERClient(dataBaseHandler,message).execute();
                else
                    ans = new ERRORmsg("registration failed").getMsg();
                break;
                
            case "LOGIN":
            	if (!this.login.get()) {
            		ans = new LOGINClient(dataBaseHandler,message).execute();
            		if (ans.substring(0, 2).equals("ACK")) {
            			this.login.set(true);
            			this.username = message[1];
            		}
            	}
            	else
            		ans = new ERRORmsg("login failed").getMsg();
            	break;
            	
            case "SIGNOUT":
            	if (this.login.get()) {
            		ans = new SIGNOUTClient(dataBaseHandler,message).execute();
            		if (ans.substring(0, 2).equals("ACK")) {
            			this.login.set(false);
            			ans = "dissconnect"; // for the server
            		}
            	}
            	else
            		ans = new ERRORmsg("signout failed").getMsg();	
            	break;
            case "REQUEST":
            	
            	break;                
        }
        if (ans.equals("dissconnect")) { //the user ask to SIGNOUT
        	this.connections.send(this.connectionId, "ACK signout succeeded");
        	this.connections.disconnect(this.connectionId);
        }
        return ans;
    }

    /**
     * when we get logout command, the client handling should be terminate.
     * @return shouldTerminate
     */
    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
    
    /**
     * aid function, get a string and return an array of strings,each cell contain a string 
     * that was separate with " " in the original msg.
     * @param msg
     * @return an array of strings that was separate with " ".
     */
    private String[] stringToArray(String msg) {
        String[] result = msg.split(" ");
        return result;
    }
    
    /**
     * End of File.
     */
}
