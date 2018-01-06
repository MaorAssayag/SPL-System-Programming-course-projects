package bgu.spl181.net.impl.Blockbuster.gsonimpl;

import java.util.concurrent.ConcurrentHashMap;

public class users {
    private ConcurrentHashMap<String,user> users;

    public users(){
        users = new ConcurrentHashMap<String, user>();
    }

    public boolean adduser(user user){
        boolean ans = users.contains(user.getUsername());
        if(!ans)
            users.put(user.getUsername(),user);
        return ans;
    }

}
