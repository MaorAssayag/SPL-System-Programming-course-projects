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
                case "balance":
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
                        ans = new ERRORmsg("request " + Commands[1] + " failed").getMsg();
                    }
                    break;
               
                case "info":
                    if(Commands.length == 2){
                        dataBaseHandler.getReadWriteLockMovie().readLock().lock();
                        MovieJson temp = new MovieJson(dataBaseHandler.getPathMovie());
                        movies movies = temp.getMovies();
                        ans = new ACKmsg("info " + movies.toString()).getMsg();
                        dataBaseHandler.getReadWriteLockMovie().readLock().unlock();
                    }
                    else if (Commands.length == 3){
                        dataBaseHandler.getReadWriteLockMovie().readLock().lock();
                        MovieJson temp = new MovieJson(dataBaseHandler.getPathMovie());
                        movies movies = temp.getMovies();
                        movie movie = movies.getMovie(Commands[2]);
                        ans = movie.toString();
                        ans = new ACKmsg("info " + ans).getMsg();
                        dataBaseHandler.getReadWriteLockMovie().readLock().unlock();
                    }
                    else{
                        ans = new ERRORmsg("request " + Commands[1] + " failed").getMsg();
                    }
                    break;
                
                case "rent":{
                	String movieName = this.getMovieName(this.Commands);
                    dataBaseHandler.getReadWriteLockMovie().writeLock().lock();
                    MovieJson temp = new MovieJson(dataBaseHandler.getPathMovie());
                    movies movies = temp.getMovies();
                    movie currentMovie = movies.getMovie(movieName);
                    
                    // is this movie exist in the system ? || no more copies of the movie that are available for rental
                    if (currentMovie == null || !currentMovie.IsThereAMovieLeft()) {
                    	ans = new ERRORmsg("request " + Commands[1] + " failed").getMsg();
                    	dataBaseHandler.getReadWriteLockMovie().writeLock().unlock();
                    	break;
                    }
                    dataBaseHandler.getReadWriteLockUsers().writeLock().lock();
                    users users = new UserJson(dataBaseHandler.getPathUsers()).getUsers();
                    user currentUser = users.GetUser(ClieantName);
                    
                    // is the user already renting the movie || does the user have enough money in their balance ?
                    // || The movie is banned in the user’s country
                    if (currentUser.isTheUserRentThisMovie(movieName) || !currentUser.CanIRent(currentMovie.getPrice())
                    		|| currentMovie.doesThisCountryForbidden(currentUser.getCountry())) {
                    	ans = new ERRORmsg("request " + Commands[1] + " failed").getMsg();
                    	dataBaseHandler.getReadWriteLockUsers().writeLock().unlock();
                    	dataBaseHandler.getReadWriteLockMovie().writeLock().unlock();
                    	break;
                    }
                    
                    //the user fit all requirements for renting the movie
                    
                }
                
                
                default:{
            	   ans = ans = new ERRORmsg("request " + Commands[2] + " failed").getMsg();
                }
            }
        }else
            ans = new ERRORmsg("request " + Commands[1] +" failed").getMsg();
        return ans;
    }
    
    /**
     * this method will get the rent request and return the name of the movie
     * i.e: {XX,XX,"The,godfather"} and return "The godfather".
     * @param array
     * @return the name of the requested movie
     */
    public String getMovieName(String[] array) {
    	String ans = "";
    	for(int i = 2; i < array.length; i++) {
    		ans +=" " + array[i];
    	}
    	ans = ans.substring(ans.indexOf('"')+1, ans.lastIndexOf('"'));
		return ans;
    }
    /**
     * End of File.
     */
}
