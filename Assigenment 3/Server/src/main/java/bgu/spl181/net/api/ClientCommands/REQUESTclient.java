package bgu.spl181.net.api.ClientCommands;

import bgu.spl181.net.api.ServerCommands.ACKmsg;
import bgu.spl181.net.api.ServerCommands.ERRORmsg;
import bgu.spl181.net.impl.Blockbuster.DataBaseHandler;
import bgu.spl181.net.impl.Blockbuster.gsonimpl.*;


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
            switch (Commands[1]) {
                case "balance": {
                    if (Commands.length == 3 && Commands[2].equals("info")) {
                        dataBaseHandler.getReadWriteLockUsers().readLock().lock();
                        users users = new UserJson(dataBaseHandler.getPathUsers()).getUsers();
                        String balance = "" + users.GetUser(ClieantName).getBalance();
                        ans = new ACKmsg("balance " + balance).getMsg();
                        dataBaseHandler.getReadWriteLockUsers().readLock().unlock();
                    } else if (Commands.length == 3 && Commands[2].equals("add") && Commands.length == 4) {
                        dataBaseHandler.getReadWriteLockUsers().writeLock().lock();
                        UserJson temp = new UserJson(dataBaseHandler.getPathUsers());
                        users users = temp.getUsers();
                        user user = users.GetUser(ClieantName);
                        user.setBalance(Commands[3]);
                        String balance = "" + user.getBalance();
                        ans = new ACKmsg("balance " + balance + " added " + Commands[3]).getMsg();
                        temp.UpdateUser(users);
                        dataBaseHandler.getReadWriteLockUsers().writeLock().unlock();
                    } else {
                        ans = new ERRORmsg("request " + Commands[2] + " failed").getMsg();
                    }
                }
                case "info":{
                    if(Commands.length == 2 ){
                        dataBaseHandler.getReadWriteLockMovie().readLock().lock();
                        MovieJson temp = new MovieJson(dataBaseHandler.getPathMovie());
                        movies movies = temp.getMovies();
                        ans = "";
                        for (movie movie:movies.getMovies()) {
                            if(ans.length() == 0)
                                ans = movie.getName();
                            else
                                ans = ans + "," + movie.getName();
                        }
                        ans = new ACKmsg("info " + ans).getMsg();
                        dataBaseHandler.getReadWriteLockMovie().readLock().unlock();
                    }
                    else if (Commands.length == 3 ){
                        dataBaseHandler.getReadWriteLockMovie().readLock().lock();
                        MovieJson temp = new MovieJson(dataBaseHandler.getPathMovie());
                        movies movies = temp.getMovies();
                        movie movie = movies.getMovie(Commands[2]);
                        
                    }
                    else{

                    }
                }
            }
        }else{
            ans = new ERRORmsg("request " + Commands[1] +" failed").getMsg();
        }
        return ans;
    }
}
