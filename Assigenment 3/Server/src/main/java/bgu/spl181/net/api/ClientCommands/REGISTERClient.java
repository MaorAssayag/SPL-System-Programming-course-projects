package bgu.spl181.net.api.ClientCommands;

import bgu.spl181.net.api.commands.ACKmsg;
import bgu.spl181.net.api.commands.ERRORmsg;
import bgu.spl181.net.impl.Blockbuster.DataBaseHandler;
import bgu.spl181.net.impl.Blockbuster.gsonimpl.UserJson;
import bgu.spl181.net.impl.Blockbuster.gsonimpl.user;
import bgu.spl181.net.impl.Blockbuster.gsonimpl.users;

public class REGISTERClient extends ClientCommandsAbstract {



   public REGISTERClient(DataBaseHandler dataBaseHandler , String [] Commands){
       this.dataBaseHandler = dataBaseHandler;
       this.Commands = Commands;
   }


    @Override
    public String execute() {
        String type = "normal";
        String country = "";
        String balance ="0";
        //check the optional data block
        for (int i = 3; i < Commands.length; i++) {
            if(Commands[i].contains("type")){
                type = Commands[i].substring(Commands[i].indexOf('"'),Commands[i].lastIndexOf('"'));
            }
            if(Commands[i].contains("country")){
                country = Commands[i].substring(Commands[i].indexOf('"'),Commands[i].lastIndexOf('"'));
            }
            if(Commands[i].contains("balance")){
                balance = Commands[i].substring(Commands[i].indexOf('"'),Commands[i].lastIndexOf('"'));
            }
        }
        if(Commands.length >= 3 && !Commands[1].isEmpty() && !Commands[2].isEmpty() ) {
            dataBaseHandler.getReadWriteLockUsers().writeLock();//lock the Users json file
            UserJson temp =new UserJson(dataBaseHandler.getPathUsers());
            users users = temp.getUsers();
            if(users.adduser(new user(Commands[1], Commands[2], type, country, balance))){
                temp.UpdateUser(users);
                return new ACKmsg().getMsg();
            }else
                return (new ERRORmsg("registration failed")).getMsg();

        }else
            return (new ERRORmsg("registration failed")).getMsg();
   }
}
