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
//    	String ans = "BR"+'"'+"check test"+'"'+" 14 "+"8";
//    	String movieName = ans.substring(ans.indexOf('"')+1,ans.indexOf('"', ans.indexOf('"')+1));
//    	System.out.println(movieName);
//    	if (ans.substring(0, 2).equals("BR"))
//    		System.out.println("true");
//    	System.out.println(ans.substring(2));
//    	String ans = "";
//    	String[] array = {"REQUEST", "return", "'"+"check","test"+'"',"blabla",'"'+"blabla"};
//    	for(int i = 2; i < array.length; i++) {
//    		ans +=" " + array[i];
//    	}
//    	ans = ans.substring(ans.indexOf('"')+1, ans.indexOf('"', ans.indexOf('"')+1));
//    	String[] array = {"REQUEST", "return", '"'+"movie","name","&","fgd"+'"',"140","20",'"'+"israel"+'"','"'+"iran"+'"','"'+"New","ziland"+'"'};
//    	String[] ans = new String[4];
//    	String name = "";
//    	int lastindex = 0;
//    	for(int i = 2; i < array.length; i++) {
//    		if (i==2) 
//    			name = array[i];
//    		else
//    		   name +=" " + array[i];
//    		if (name.lastIndexOf('"')==name.length()-1) {
//    			lastindex = i;
//    			break;
//    		}
//    	}
//    	System.out.println(name);
//    	name = name.substring(name.indexOf('"')+1, name.indexOf('"', name.indexOf('"')+1));
//    	ans[0] = name;
//    	ans[1] = array[lastindex+1];
//    	ans[2] = array [lastindex +2];
//    	System.out.println(ans[0]);
//    	System.out.println(ans[1]);
//    	System.out.println(ans[2]);
//    	System.out.println(Integer.valueOf(ans[2]));
//    	ans[3] = "" + (lastindex + 3) ;
//    	String startFrom = ans[3];
//    	String[] command = array;
//    	int position = Integer.valueOf(startFrom);
//    	String[] answer = new String[command.length-position];
//    	System.out.println(answer.length);
//    	System.out.println("start check from :" +position);
//    	String currName = "";
//    	int last = 0;
//    	int currentCountry = 0;
//    	while (position < command.length) {
//    		System.out.println("current position while :"+position);
//        	for(int i = position; i<command.length; i++) {
//        		if (i==position) 
//        			currName = array[i];
//        		else
//        			currName +=" " + array[i];
//        		if (currName.lastIndexOf('"')==currName.length()-1) {
//        			position = i+1;
//        			break;
//        		}
//        	}
//        	answer[currentCountry] = currName.substring(1,currName.length()-1);
//        	currentCountry++;
//        	currName = "";
//    	}
//    	System.out.println(answer[0]);
//    	System.out.println(answer[1]);
//    	System.out.println(answer[2]);
//    	System.out.println(answer[3]);
//    	String[] array = {"REQUEST", "return", '"'+"check","test"+'"',"blabla",'"'+"blabla"+'"'};
//    	String ans = "";
//    	for(int i = 2; i < array.length; i++) {
//    		ans +=" " + array[i];
//    	}
//    	ans = ans.substring(ans.indexOf('"')+1, ans.indexOf('"', ans.indexOf('"')+1));
//    	System.out.println(ans);
    	
//    	String ans ="";
//    	String[] command = {"REQUEST", "return", "'"+"check","test"+'"',"10"};
//    	System.out.println(command.length);
//    	for (int i = 3; i< command.length; i++) {
//    		if (command[i].lastIndexOf('"')==command[i].length()-1) {
//    			ans = command[i+1];
//    			break;
//    		}
//    	}
//    	System.out.println(ans);
    	String[] test = {"bla","bla","bla"};
    	for (int i = 0; i < test.length; i++) {
			System.out.println(test[i]);
		}

    	
    	
//        users u =new UserJson(path).getUsers();
//        new UserJson("C:/Users/USER/Desktop/test/Userstest.json").UpdateUser(u);
    }
}
