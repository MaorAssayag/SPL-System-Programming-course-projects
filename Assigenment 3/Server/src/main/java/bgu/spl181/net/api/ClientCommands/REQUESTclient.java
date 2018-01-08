package bgu.spl181.net.api.ClientCommands;

import bgu.spl181.net.api.ServerCommands.ACKmsg;
import bgu.spl181.net.api.ServerCommands.ERRORmsg;
import bgu.spl181.net.impl.Blockbuster.DataBaseHandler;
import bgu.spl181.net.impl.Blockbuster.gsonimpl.UserJson;
import bgu.spl181.net.impl.Blockbuster.gsonimpl.user;
import bgu.spl181.net.impl.Blockbuster.gsonimpl.users;


public class REQUESTclient extends ClientCommandsAbstract {
    private String ClieantName;

    public REQUESTclient(DataBaseHandler dataBaseHandler , String[] Commands ,String ClientName){
        this.dataBaseHandler = dataBaseHandler;
        this.ClieantName = ClientName;
        this.Commands = Commands;
    }


    @Override
    public String execute() {
        String ans ="";
        if(Commands.length>=2){
            switch (Commands[1]){
                case "balance":
                    if(Commands.length == 3 && Commands[2].equals("info")){
                        dataBaseHandler.getReadWriteLockUsers().readLock().lock();
                        users users = new UserJson(dataBaseHandler.getPathUsers()).getUsers();
                        String balance ="" + users.GetUser(ClieantName).getBalance();
                        ans = new ACKmsg("balance" + balance).getMsg();
                        dataBaseHandler.getReadWriteLockUsers().readLock().unlock();
                    }else if(Commands.length == 3 && Commands[3].equals("add") && Commands.length == 4 ){
                        dataBaseHandler.getReadWriteLockUsers().writeLock().lock();
                        users users = new UserJson(dataBaseHandler.getPathUsers()).getUsers();
                        user user =  users.GetUser(ClieantName);

                        //String balance ="" + users.GetUser(ClieantName).getBalance();
                        ans = new ACKmsg("balance" + balance).getMsg();
                        dataBaseHandler.getReadWriteLockUsers().writeLock().unlock();
                    }
                    else
                        ans = new ERRORmsg("request" + Commands[2] +"failed").getMsg();
            }
        }else{
            ans = new ERRORmsg("request" + Commands[1] +"failed").getMsg();
        }
        return ans;
    }
}
