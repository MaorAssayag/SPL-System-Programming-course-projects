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
                    } else if (Commands.length == 4 && Commands[2].equals("add")) {
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

                case "info": {
                    if (Commands.length == 2) { //get all the movies name as a string 
                        dataBaseHandler.getReadWriteLockMovie().readLock().lock();
                        MovieJson temp = new MovieJson(dataBaseHandler.getPathMovie());
                        movies movies = temp.getMovies();
                        ans = new ACKmsg("info " + movies.toString()).getMsg();
                    } else{ // get a specsific movie info string
                        dataBaseHandler.getReadWriteLockMovie().readLock().lock();
                        MovieJson temp = new MovieJson(dataBaseHandler.getPathMovie());
                        movies movies = temp.getMovies();
                        String moviename = getMovieName(Commands);
                        movie movie = movies.getMovie(moviename);
                        if(movie !=null) {
                            ans = movie.toString();
                            ans = new ACKmsg("info " + ans).getMsg();
                        }
                        else
                            ans = new ERRORmsg("request " + Commands[1] + " failed").getMsg();
                    }
                    dataBaseHandler.getReadWriteLockMovie().readLock().unlock();

                }
                    break;

                case "rent":{
                	String movieName = this.getMovieName(this.Commands);
                    dataBaseHandler.getReadWriteLockMovie().writeLock().lock();
                    MovieJson temp = new MovieJson(dataBaseHandler.getPathMovie());
                    movies currentMovies = temp.getMovies();
                    movie currentMovie = currentMovies.getMovie(movieName);

                    // is this movie exist in the system ? || no more copies of the movie that are available for rental
                    if (currentMovie == null || !currentMovie.IsThereAMovieLeft()) {
                    	ans = new ERRORmsg("request " + Commands[1] + " failed").getMsg();
                    	dataBaseHandler.getReadWriteLockMovie().writeLock().unlock();
                    	break;
                    }
                    dataBaseHandler.getReadWriteLockUsers().writeLock().lock();
                    UserJson temp2 = new UserJson(dataBaseHandler.getPathUsers());
                    users currentUsers = temp2.getUsers();
                    user currentUser = currentUsers.GetUser(ClieantName);

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
                    currentUser.addmovie(currentMovie);
                    currentMovie.RentThisMovie();
                    temp2.UpdateUser(currentUsers);
                    temp.UpdateMovies(currentMovies);
                	//ans = new ACKmsg("rent "+ '"'+movieName+'"' +" success").getMsg(); - we send those later
                	ans = "BR1"+currentMovie.broadcastToString();
                	dataBaseHandler.getReadWriteLockUsers().writeLock().unlock();
                	dataBaseHandler.getReadWriteLockMovie().writeLock().unlock();
                	}
                	break;

                case "return":{
                	String movieName = this.getMovieName(this.Commands);
                    dataBaseHandler.getReadWriteLockMovie().writeLock().lock();
                    MovieJson temp = new MovieJson(dataBaseHandler.getPathMovie());
                    movies currentMovies = temp.getMovies();
                    movie currentMovie = currentMovies.getMovie(movieName);

                    // is this movie exist in the system ?
                    if (currentMovie == null) {
                    	ans = new ERRORmsg("request " + Commands[1] + " failed").getMsg();
                    	dataBaseHandler.getReadWriteLockMovie().writeLock().unlock();
                    	break;
                    }
                    dataBaseHandler.getReadWriteLockUsers().writeLock().lock();
                    UserJson temp2 = new UserJson(dataBaseHandler.getPathUsers());
                    users currentUsers = temp2.getUsers();
                    user currentUser = currentUsers.GetUser(ClieantName);

                    // is the user already renting the movie
                    if (!currentUser.ReturnMovie(movieName)) {
                    	ans = new ERRORmsg("request " + Commands[1] + " failed").getMsg();
                    	dataBaseHandler.getReadWriteLockUsers().writeLock().unlock();
                    	dataBaseHandler.getReadWriteLockMovie().writeLock().unlock();
                    	break;
                    }

                    //the user fit all requirements for returning the movie
                    currentMovie.ReturnThisMovie();
                    temp2.UpdateUser(currentUsers);
                    temp.UpdateMovies(currentMovies);
                	//ans = new ACKmsg("return "+ '"'+movieName+'"' +" success").getMsg(); - we send those later
                    //BROADCAST movie <"movie name"> < No. copies left > <price>
                	ans = "BR2"+currentMovie.broadcastToString();
                	dataBaseHandler.getReadWriteLockUsers().writeLock().unlock();
                	dataBaseHandler.getReadWriteLockMovie().writeLock().unlock();
                	}
                	break;

                case "addmovie":{
                	//does this user admin?
                    dataBaseHandler.getReadWriteLockUsers().readLock().lock();
                    UserJson temp2 = new UserJson(dataBaseHandler.getPathUsers());
                    users currentUsers = temp2.getUsers();
                    user currentUser = currentUsers.GetUser(ClieantName);
                    if (!currentUser.getType().equals("admin")) {
                    	ans = new ERRORmsg("request " + Commands[1] + " failed").getMsg();
                    	dataBaseHandler.getReadWriteLockUsers().readLock().unlock();
                    	break;
                    }
                	String[] movieData = this.getMovieData(this.Commands);
                	String movieName = movieData[0];
                    dataBaseHandler.getReadWriteLockMovie().writeLock().lock();
                    MovieJson temp1 = new MovieJson(dataBaseHandler.getPathMovie());
                    movies movies = temp1.getMovies();
                    // does this movie exist? || the movie price <= 0
                    if (movies.getMovie(movieName) != null || Integer.valueOf(movieData[2]) <= 0) {
                    	ans = new ERRORmsg("request " + Commands[1] + " failed").getMsg();
                    	dataBaseHandler.getReadWriteLockUsers().readLock().unlock();
                        dataBaseHandler.getReadWriteLockMovie().writeLock().unlock();
                    	break;
                    }
                   
                    //add the movie
                    String[] bannedCountries = this.extractBannedcountry(this.Commands, movieData[3]);
                    movie currentMovie = new movie(movieName, movieData[2], bannedCountries, movieData[1]);
                    movies.AddMovie(currentMovie);
                    temp1.UpdateMovies(movies);
                	dataBaseHandler.getReadWriteLockUsers().readLock().unlock();
                    dataBaseHandler.getReadWriteLockMovie().writeLock().unlock();
                	ans = "BR3"+currentMovie.broadcastToString(); // for later handling and broadcsting
                	}
                	break;

                case "remmovie":{
                	//does this user admin?
                    dataBaseHandler.getReadWriteLockUsers().readLock().lock();
                    UserJson temp2 = new UserJson(dataBaseHandler.getPathUsers());
                    users currentUsers = temp2.getUsers();
                    user currentUser = currentUsers.GetUser(ClieantName);
                    if (!currentUser.getType().equals("admin")) {
                    	ans = new ERRORmsg("request " + Commands[1] + " failed").getMsg();
                    	dataBaseHandler.getReadWriteLockUsers().readLock().unlock();
                    	break;
                    }

                	String movieName = this.getMovieName(this.Commands);
                    dataBaseHandler.getReadWriteLockMovie().writeLock().lock();
                    MovieJson temp1 = new MovieJson(dataBaseHandler.getPathMovie());
                    movies movies = temp1.getMovies();
                    movie currMovie = movies.getMovie(movieName);

                    // does this movie exist? || someone retnting this movie
                    if (currMovie == null || currMovie.isSomeoneRetning()) {
                    	ans = new ERRORmsg("request " + Commands[1] + " failed").getMsg();
                    	dataBaseHandler.getReadWriteLockUsers().readLock().unlock();
                        dataBaseHandler.getReadWriteLockMovie().writeLock().unlock();
                    	break;
                    }

                    //remove this movie
                    movies.RemoveMovie(movieName);
                    temp1.UpdateMovies(movies);
                	dataBaseHandler.getReadWriteLockUsers().readLock().unlock();
                    dataBaseHandler.getReadWriteLockMovie().writeLock().unlock();
                	ans = "BR4"+currMovie.broadcastToString(); // for later handling and broadcsting
                }
                	break;

                case "changeprice":{
                	//does this user admin?
                    dataBaseHandler.getReadWriteLockUsers().readLock().lock();
                    UserJson temp2 = new UserJson(dataBaseHandler.getPathUsers());
                    users currentUsers = temp2.getUsers();
                    user currentUser = currentUsers.GetUser(ClieantName);
                    if (!currentUser.getType().equals("admin")) {
                    	ans = new ERRORmsg("request " + Commands[1] + " failed").getMsg();
                    	dataBaseHandler.getReadWriteLockUsers().readLock().unlock();
                    	break;
                    }

                	String movieName = this.getMovieName(this.Commands);
                    dataBaseHandler.getReadWriteLockMovie().writeLock().lock();
                    MovieJson temp1 = new MovieJson(dataBaseHandler.getPathMovie());
                    movies movies = temp1.getMovies();
                    movie currMovie = movies.getMovie(movieName);
                    String newPrice = this.getNewPrice(this.Commands);

                    // does this movie exist? || the new price <= 0
                    if (currMovie == null || newPrice == null || Integer.valueOf(newPrice) <= 0) {
                    	ans = new ERRORmsg("request " + Commands[1] + " failed").getMsg();
                    	dataBaseHandler.getReadWriteLockUsers().readLock().unlock();
                        dataBaseHandler.getReadWriteLockMovie().writeLock().unlock();
                    	break;
                    }

                    //change this movie price
                    currMovie.setPrice(Integer.valueOf(newPrice));
                    temp1.UpdateMovies(movies);
                	dataBaseHandler.getReadWriteLockUsers().readLock().unlock();
                    dataBaseHandler.getReadWriteLockMovie().writeLock().unlock();
                	ans = "BR5"+currMovie.broadcastToString(); // for later handling and broadcsting
                }
                	break;
                default:
            	   ans = new ERRORmsg("request " + Commands[2] + " failed").getMsg();
            	   break;
            }
        }else
            ans = new ERRORmsg("request " + Commands[1] +" failed").getMsg();
        return ans;
    }

    /**
     * this method will get the rent request and return the name of the movie for the rent,return and remmovie commands.
     * i.e: {XX,XX,"The,godfather"} and return a string <The godfather>.
     * @param array
     * @return the name of the requested movie
     */
    public String getMovieName(String[] array) {
    	String ans = "";
    	for(int i = 2; i < array.length; i++) {
    		ans +=" " + array[i];
    	}
    	ans = ans.substring(ans.indexOf('"')+1, ans.indexOf('"', ans.indexOf('"')+1));
		return ans;
    }

    /**
     * get the command array and retun {<movie name>,<amount of copies>,<price>,<start index of banned country>}
     * @param array
     * @return
     */
    public String[] getMovieData(String[] array) {
    	String[] ans = new String[4];
    	String name = "";
    	int lastindex = 0;
    	for(int i = 2; i < array.length; i++) {
    		if (i==2)
    			name = array[i];
    		else
    		   name +=" " + array[i];
    		if (name.lastIndexOf('"')==name.length()-1) {
    			lastindex = i;
    			break;
    		}
    	}
    	name = name.substring(name.indexOf('"')+1, name.indexOf('"', name.indexOf('"')+1));
    	ans[0] = name;
    	ans[1] = array[lastindex+1];
    	ans[2] = array [lastindex +2];
    	ans[3] = "" + (lastindex+3);
		return ans;
    }

    /**
     * this method will get the command from the client and
     * return the banned country array {<country name>,<country name>,..}
     * @param command
     * @param startFrom
     * @return the banned country array {<country name>,<country name>,..}
     */
    public String[] extractBannedcountry(String[] command, String startFrom) {
    	int position = Integer.valueOf(startFrom);
    	String[] answer = new String[command.length-position];
    	String currName = "";
    	int last = 0;
    	int currentCountry = 0;
    	while (position < command.length) {
        	for(int i = position; i<command.length; i++) {
        		if (i==position)
        			currName = command[i];
        		else
        			currName +=" " + command[i];
        		if (currName.lastIndexOf('"')==currName.length()-1) {
        			position = i+1;
        			break;
        		}
        	}
        	answer[currentCountry] = currName.substring(1,currName.length()-1);
        	currentCountry++;
        	currName = "";
    	}
    	return answer;
    }

    public String getNewPrice(String[] command) {
    	String ans ="";
    	if (command.length < 4)
    		return null;
    	for (int i = 3; i< command.length; i++) {
    		if (command[i].lastIndexOf('"')==command[i].length()-1) {
    			ans = command[i+1];
    		}
    	}
    	return ans;
    }
    /**
     * End of File.
     */
}
