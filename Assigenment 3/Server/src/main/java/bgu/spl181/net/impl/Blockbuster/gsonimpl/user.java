package bgu.spl181.net.impl.Blockbuster.gsonimpl;

import com.google.gson.annotations.Since;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class user {
    @Since(1) private String username;
    @Since(1)  private String type;
    @Since(1)  private String password;
    @Since(1)  private String country;
    @Since(1)  private ArrayList<UserMovie> movies;
    @Since(1)  private String balance;

    public user(String username, String password, String type, String country, String balance){
        this.username = username;
        this.password = password;
        this.type = type;
        this.country = country;
        this.balance = balance;
        this.movies = new ArrayList<UserMovie>();
    }

    public void addmovie(movie movie){
        balance = "" + (Integer.valueOf(balance) - Integer.valueOf(movie.getPrice()));
        movies.add(new UserMovie("" + movie.getId(),movie.getName()));
    }
    public boolean CanIRent(int price){
        return getBalance() - price >= 0;
    }
    public boolean ReturnMovie(String MovieName){
        boolean remove = false;
        for (UserMovie u:movies) {
            if(u.getName() == MovieName){
                movies.remove(u);
                remove = true;
                break;
            }
        }
        return remove;
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
        return Integer.valueOf(balance);
    }

}