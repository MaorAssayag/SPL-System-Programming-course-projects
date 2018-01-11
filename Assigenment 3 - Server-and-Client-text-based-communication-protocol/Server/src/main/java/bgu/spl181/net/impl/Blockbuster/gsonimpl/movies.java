package bgu.spl181.net.impl.Blockbuster.gsonimpl;

import com.google.gson.annotations.Since;

import java.util.concurrent.ConcurrentHashMap;

/**
 * This class represent an array&hashmap of movie object's for the json update conversion.
 * data member :
 * 		movies := an array of movie object's.
 * 		Ids := current unique id for a new movie.
 * 		movieshash := database for all the active movies that the server contain.
 */
public class movies {
    @Since(2) private ConcurrentHashMap <String,movie> movieshash;
    @Since(1) private movie[] movies ;
    @Since(2) private int Ids;

    /**
     * default constructor.
     */
    public movies(){
        this.movieshash = new ConcurrentHashMap<String, movie>();
        this.Ids = 1;
    }
    
    /**
     * add a movie to the active database.
     * @param Movie
     * @return boolean if the movie was been added to the database.
     */
    public boolean AddMovie(movie Movie){
        boolean ans = movieshash.containsKey(Movie);
        if(!ans)
            Movie.setId(this.Ids++);
        this.movieshash.put(Movie.getName(),Movie);
        return ans;
    }

    /**
     * get the movie object by the movie name.
     * @param Movie
     * @return movie object that belongs to the Movie name.
     */
    public movie getMovie(String Movie) {
        return this.movieshash.get(Movie);
    }
    
    /**
     * remove the movie object that belongs to the Movie name.
     * @param Movie
     * @return boolean if the movie was been removed to the database.
     */
    public boolean RemoveMovie(String Movie){
        boolean ans = movieshash.remove(Movie) != null;
        if(ans){
            this.Ids = 1;
            for (String movie : this.movieshash.keySet()) {
                movieshash.get(movie).setId(this.Ids);
                this.Ids++;
            }
        }
        return ans;
    }

    /**
     * @return the available current unique id.
     */
    public int getIds() {
        return this.Ids;
    }

    /**
     * convert an array of movies to hash map.
     */
    public void updateMoviesJson() {//for the update movies Hash after json
        for (int i = 0; i < this.movies.length; i++) {
            AddMovie(this.movies[i]);
        }
    }
    
    /**
     * convert an hashmap of movies to array.
     */
    public void makemoviesarrayforjson(){
        movie [] ans = new movie[getIds()-1];
        int i = 0;
        for (String movie : this.movieshash.keySet()) {
            ans[i] = this.movieshash.get(movie);
            i++;
        }
        this.movies = ans;
    }
    /**
     * @return the  movie array.
     */
    public movie[] getMovies() {
        return movies;
    }
    /**
     * @return {@link String} of movies name.
     */

    @Override
    public String toString() {
        String ans = "";
        for (movie movie:getMovies()) {
            if(ans.length() == 0)
                ans ='"'+ movie.getName()+'"';
            else
                ans = ans + " "+ '"' +movie.getName()+'"';
        }
        return ans;
    }

    /**
     * End of File.
     */
}