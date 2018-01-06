package bgu.spl181.net.impl.Blockbuster.gsonimpl;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class test  {
    public static void main(String[] args){
        String path ="C:/Users/USER/Desktop/test/a.txt";
        String [] con ={"a","c","f"};
        movies movies = new movies();
        movies.AddMovie(new movie(
                "a",
                3,
                con,
                3
        ));
        movies.AddMovie(new movie(
                "b",
                4,
                con,
                5
        ));
        movies.AddMovie(new movie(
                "c",
                5,
                con,
                4
        ));
        ReadWriteLock a= new ReentrantReadWriteLock();
        new MovieJson(path , a).UpdateMovies(movies);
        movies m =new MovieJson(path,a).getMovies();
    }
}
