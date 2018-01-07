package bgu.spl181.net.impl.Blockbuster.gsonimpl;

import com.google.gson.annotations.Since;

import java.util.concurrent.ConcurrentHashMap;

public class movies {
    @Since(2)private ConcurrentHashMap <String,movie> movieshash;
    @Since(1) private movie [] movies ;
    @Since(2)  private int Ids;

    public movies(){
        movieshash = new ConcurrentHashMap<String, movie>();
        Ids = 1;
    }
    public boolean AddMovie(movie Movie){
        boolean ans = movieshash.contains(Movie);
        if(!ans)
            Movie.setId(Ids++);
        movieshash.put(Movie.getName(),Movie);
         return ans;
    }

    public movie getMovie(String Movie) {
        return movieshash.get(Movie);
    }
    public boolean RemoveMovie(String Movie){
        boolean ans = movieshash.remove(Movie) != null;
        if(ans){
            Ids = 1;
            for (String movie :movieshash.keySet()) {
                movieshash.get(movie).setId(Ids);
                Ids++;
            }
        }
        return ans;
    }

    public int getIds() {
        return Ids;
    }

    public void updateMoviesJson() {//for the update movies Hash after Json
        for (int i = 0; i < movies.length; i++) {
            AddMovie(movies[i]);
        }
    }
    public void makemoviesarrayforjson(){
        movie [] ans = new movie[getIds()-1];
        int i = 0;
        for (String movie:movieshash.keySet()) {
            ans[i] = movieshash.get(movie);
            i++;
        }
        movies = ans;
    }

}
