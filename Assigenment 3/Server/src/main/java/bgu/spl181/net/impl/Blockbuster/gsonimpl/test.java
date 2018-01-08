package bgu.spl181.net.impl.Blockbuster.gsonimpl;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class test  {
    public static void main(String[] args){
//        String a = "545";
//        System.out.println(Integer.valueOf(a));
////        String [] con = {"c","d"};
//        movie movie = new movie("a",
//                "b",
//                con,
//                "1");
//        System.out.println(movie);
//        String path ="C:/Users/USER/Desktop/test/Users.json";
//        String [] con ={"a","c","f"};
//        movies movies = new movies();
//        movies.AddMovie(new movie(
//                "a",
//                "3",
//                con,
//                "3"
//        ));
//        movies.AddMovie(new movie(
//                "b",
//                "4",
//                con,
//                "5"
//        ));
//        movies.AddMovie(new movie(
//                "c",
//                "5",
//                con,
//                "4"
//        ));
//        movie movie =  movies.getMovie("c");
//        movie.setPrice(123);
    	String ans = "BR"+'"'+"check test"+'"'+" 14 "+"8";
    	String movieName = ans.substring(ans.indexOf('"')+1,ans.indexOf('"', ans.indexOf('"')+1));
    	System.out.println(movieName);
    	if (ans.substring(0, 2).equals("BR"))
    		System.out.println("true");
    	System.out.println(ans.substring(2));
//        users u =new UserJson(path).getUsers();
//        new UserJson("C:/Users/USER/Desktop/test/Userstest.json").UpdateUser(u);
    }
}
