package bgu.spl181.net.impl.Blockbuster.gsonimpl;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class MovieJson {
    private String Path;
    private Gson gsonread;
    private Gson gsonwrite;

    public  MovieJson(String path){
        this.Path = path;
        gsonread =  new GsonBuilder().setPrettyPrinting().setVersion(1).create();
        gsonwrite = new GsonBuilder().setPrettyPrinting().setVersion(1).create();
    }

    //get the movies from the json file
    public movies getMovies() {
        movies movies = null;
        try (Reader reader = new FileReader(Path)){
            movies = gsonread.fromJson(reader,movies.class);
            movies.updateMoviesJson();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    return movies;
    }

    public void UpdateMovies(movies movies){
        try (Writer writer = new FileWriter(Path)){
            movies.makemoviesarrayforjson();
            gsonwrite.toJson(movies, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}