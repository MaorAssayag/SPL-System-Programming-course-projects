package bgu.spl181.net.impl.Blockbuster.gsonimpl;

import java.util.concurrent.ConcurrentHashMap;

public class movies {
    private ConcurrentHashMap <String,movie> movies;
    int Ids;

    public movies(){
        movies = new ConcurrentHashMap<String, movie>();
        Ids = 0;
    }
    public boolean AddMovie(movie Movie){
        boolean ans = movies.contains(Movie);
        if(!ans)
            Movie.setId(Ids++);
         movies.put(Movie.getName(),Movie);
         return ans;
    }

    public movie getMovie(String Movie) {
        return movies.get(Movie);
    }
    public boolean RemoveMovie(String Movie){
        boolean ans = movies.remove(Movie) != null;
        if(ans){
            Ids = 1;
            for (String movie :movies.keySet()) {
                movies.get(movie).setId(Ids);
                Ids++;
            }
        }
        return ans;
    }
}
