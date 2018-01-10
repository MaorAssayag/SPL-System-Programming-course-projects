package bgu.spl181.net.impl.rci;

import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;

public class test {
	String id;
	int num;
	String command;
	
	public test(String id, int num, String command) {
		this.id = id;
		this.num = num;
		this.command = command;
	}
	
public static void main(String[] args) throws JsonIOException, IOException {
	Gson gson = new GsonBuilder().setPrettyPrinting().create();

	test obj = new test("maor", 14, "try");
	
	// 1. Java object to JSON, and save into a file
	try {
		String json = gson.toJson(obj);
		gson.toJson(json, new FileWriter("C:/Users/USER/Documents/GitHub/SPL-System-Programming-course-projects/SPL-System-Programming-course-projects/Assigenment 3/test/users.json"));
	} catch (Exception e) {
		System.out.println("fail");	
}
	

	// 2. Java object to JSON, and assign to a String
	String jsonInString = gson.toJson(obj);
	System.out.println(jsonInString);

}
}
