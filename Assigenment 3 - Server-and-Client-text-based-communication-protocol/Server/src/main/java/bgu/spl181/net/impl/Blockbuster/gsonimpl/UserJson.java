package bgu.spl181.net.impl.Blockbuster.gsonimpl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

/**
 * This class is aid class for get&set the data in the user database json file.
 * data members :
 * 		path := the current path of the user database json file.
 * 		gsonread := GsonBuilder for reading the json file.
 * 		gsonwrite := GsonBuilder for writing the json file.
 */
public class UserJson {
    private String Path;
    private Gson gsonread;
    private Gson gsonwrite;

    /**
     * default constructor.
     * @param path
     */
    public  UserJson(String path){
        this.Path = path;
        gsonread =  new GsonBuilder().setPrettyPrinting().setVersion(1).create();
        gsonwrite = new GsonBuilder().setPrettyPrinting().setVersion(1).create();
    }

    /**
     * getter for the user data from the json file.
     * @return users Object which contain an array of user Object's.
     */
    public users getUsers() {
        users users = null;
        try (Reader reader = new FileReader(Path)){
            users = gsonread.fromJson(reader,users.class);
            if(users != null)
                users.updateusersJson();
            else{
                users = new users();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * setter(update) for the user data from the json file.
     * @param users
     */
    public void UpdateUser(users users){
        try (Writer writer = new FileWriter(Path)){
            users.makeusersarrayforjson();
            gsonwrite.toJson(users, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * End of File.
     */
}