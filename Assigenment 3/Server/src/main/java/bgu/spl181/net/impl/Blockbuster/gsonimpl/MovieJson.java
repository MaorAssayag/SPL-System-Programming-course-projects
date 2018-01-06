package bgu.spl181.net.impl.Blockbuster.gsonimpl;


import com.google.gson.Gson;

import java.io.*;
import java.util.concurrent.locks.ReadWriteLock;

public class MovieJson {
    private String Path;
    private ReadWriteLock ReadWriteLock;

    public  MovieJson(String path , ReadWriteLock ReadWriteLock){
        this.ReadWriteLock = ReadWriteLock;
        this.Path = path;
    }

    //get the movies from the json file
    public movies getMovies() {
        movies movies = null;
        try (Reader reader = new FileReader(Path)){
            movies = new Gson().fromJson(reader, movies.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    return movies;
    }

    public void UpdateMovies(movies movies){
        ReadWriteLock.writeLock();
        try (Writer writer = new FileWriter(Path)){
           writer.write(new Gson().toJson(movies));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
