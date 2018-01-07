package bgu.spl181.net.impl.Blockbuster.gsonimpl;

import com.google.gson.annotations.Since;

import java.util.concurrent.ConcurrentHashMap;

public class users {
    @Since(2) private ConcurrentHashMap<String,user> usersHash;
    @Since(1) private user [] users;

    public users(){
        usersHash = new ConcurrentHashMap<String, user>();
    }

    public boolean adduser(user user){
        boolean ans = usersHash.contains(user.getUsername());
        if(!ans)
            usersHash.put(user.getUsername(),user);
        return ans;
    }

    public void updateusersJson() {//for the update users Hash after Json
        for (int i = 0; i < users.length; i++) {
            adduser(users[i]);
        }
    }
    public void makeusersarrayforjson(){//make the Users ready to json file
        user [] ans = new user[usersHash.size()];
        int i = 0;
        for (String users:usersHash.keySet()) {
            ans[i] = usersHash.get(users);
            i++;
        }
        users = ans;
    }


}
