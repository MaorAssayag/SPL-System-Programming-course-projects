package bgu.spl181.net.impl.Blockbuster.gsonimpl;

import com.google.gson.annotations.Since;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This class represent a user object with its information.
 * data member :
 *  	username := the username that this client will be registered in to the server.
 *  	type := is this client is a normal user or a admin.
 *  	password := the password that this client will be registered in to the server.
 *  	country := the country of the user.
 * 	 	movies := the movies that this user is renting.
 * 		balance := the current balance left for the user.
 */
public class user {
    @Since(1) private String username;
    @Since(1) private String type;
    @Since(1) private String password;
    @Since(1) private String country;
    @Since(1) private ArrayList<UserMovie> movies;
    @Since(1) private String balance;

    /**
     * default constructor.
     * @param username
     * @param password
     * @param type
     * @param country
     * @param balance
     */
    public user(String username, String password, String type, String country, String balance){
        this.username = username;
        this.password = password;
        this.type = type;
        this.country = country;
        this.balance = balance;
        this.movies = new ArrayList<UserMovie>();
    }

    /**
     * add movie to the movie rental list of this user.
     * @param movie
     */
    public void addmovie(movie movie){
        balance = "" + (Integer.valueOf(balance) - Integer.valueOf(movie.getPrice()));
        movies.add(new UserMovie("" + movie.getId(),movie.getName()));
    }
    
    /**
     * @param price
     * @return boolean if the user can rent a movie that cost 'price' amount.
     */
    public boolean CanIRent(int price){
        return getBalance() - price >= 0;
    }
    
    /**
     * remove the 'MovieName' from the rent list of movies of the user.
     * @param MovieName
     * @return boolean if the method successfully remove this movie from the list.
     */
    public boolean ReturnMovie(String MovieName){
        boolean remove = false;
        for (UserMovie u:movies) {
            if(u.getName().equals(MovieName)){
                movies.remove(u);
                remove = true;
                break;
            }
        }
        return remove;
    }

    /**
     * getter method of the type data member - admin/regular client.
     * @return the user type.
     */
    public String getType() {
        return this.type;
    }

    /**
     * getter method of the username data member.
     * @return the user username.
     */
    public String getUsername() {
        return this.username;
    }
    
    /**
     * getter method of the country data member.
     * @return the user username.
     */
    public String getCountry() {
        return this.country;
    }

    /**
     * getter method of the password data member.
     * @param password
     * @return the user password.
     */
    public Boolean PasswordCorrect(String password){
        return (this.password.equals(password));
    }

    /**
     * getter method of the balance data member.
     * @return the current balance of this user.
     */
    public int getBalance() {
        return Integer.valueOf(balance);
    }

    public void setBalance(String balance) {
        this.balance = "" + (Integer.valueOf(this.balance) + Integer.valueOf(balance));
    }

    /**
     * method that check if the user is already renting the movie 'movieName'
     * @param movieName
     * @return true if the user already renting this movie
     */
    public boolean isTheUserRentThisMovie(String movieName) {
    	boolean ans = false;
        for (UserMovie u:movies) {
            if(u.getName().equals(movieName)){
            	ans = true;
                break;
            }
        }
		return ans;
    }

    /**
     * End of file.
     */
}