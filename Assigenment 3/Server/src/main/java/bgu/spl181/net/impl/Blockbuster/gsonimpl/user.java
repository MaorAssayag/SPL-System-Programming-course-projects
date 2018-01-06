package bgu.spl181.net.impl.Blockbuster.gsonimpl;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class user {
    private String username;
    private String password;
    private String type;
    private String country;
    private HashMap<String,movie> movies;
    private int balance;

    public user(String username, String password, String type, String country, int balance){
        this.username = username;
        this.password = password;
        this.type = type;
        this.country = country;
        this.balance = balance;
        this.movies = new HashMap<String, movie>();
    }

    public void addmovie(movie movie){
        balance = balance - movie.getPrice();
        movies.put(movie.getName(),movie);
    }
    public boolean CanIRent(int price){
        return balance - price >= 0;
    }
    public boolean ReturnMovie(String MovieName){
        return (movies.remove(MovieName) != null);
    }

    public String getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public Boolean PasswordCorrect(String Password){
        return Password == password;
    }

    public int getBalance() {
        return balance;
    }

}
