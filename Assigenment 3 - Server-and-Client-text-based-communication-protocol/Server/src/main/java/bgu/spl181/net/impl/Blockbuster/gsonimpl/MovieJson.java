package bgu.spl181.net.impl.Blockbuster.gsonimpl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

/**
 * This class is aid class for get&set the data in the movie database json file.
 * data members :
 * 		path := the current path of the movie database json file.
 * 		gsonread := GsonBuilder for reading the json file.
 * 		gsonwrite := GsonBuilder for writing the json file.
 */
public class MovieJson {
    private String path;
    private Gson gsonread;
    private Gson gsonwrite;

    /**
     * default constructor.
     * @param path of the movie database json file.
     */
    public  MovieJson(String path){
        this.path = path;
        gsonread =  new GsonBuilder().setPrettyPrinting().setVersion(1).create();
        gsonwrite = new GsonBuilder().setPrettyPrinting().setVersion(1).create();
    }

    /**
     * getter for the movie data from the json file.
     * @return movies Object which contain an array of movie Object's.
     */
    public movies getMovies() {
        movies movies = null;
        try (Reader reader = new FileReader(this.path)){
            movies = gsonread.fromJson(reader,movies.class);
            if(movies != null)
                movies.updateMoviesJson();
            else
                movies = new movies();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return movies;
    }

    /**
     * setter(update) for the movie data from the json file.
     * @param movies
     */
    public void UpdateMovies(movies movies){
        try (Writer writer = new FileWriter(this.path)){
            movies.makemoviesarrayforjson();
            gsonwrite.toJson(movies, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * End of File.
     */
}