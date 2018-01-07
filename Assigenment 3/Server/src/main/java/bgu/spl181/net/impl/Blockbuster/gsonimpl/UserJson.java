package bgu.spl181.net.impl.Blockbuster.gsonimpl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class UserJson {
    private String Path;
    private Gson gsonread;
    private Gson gsonwrite;

    public  UserJson(String path){
        this.Path = path;
        gsonread =  new GsonBuilder().setPrettyPrinting().setVersion(1).create();
        gsonwrite = new GsonBuilder().setPrettyPrinting().setVersion(1).create();
    }

    //get the users from the json file
    public users getUsers() {
        users users = null;
        try (Reader reader = new FileReader(Path)){
            users = gsonread.fromJson(reader,users.class);
            users.updateusersJson();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void UpdateUser(users users){
        try (Writer writer = new FileWriter(Path)){
            users.makeusersarrayforjson();
            gsonwrite.toJson(users, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
