package bgu.spl181.net.impl.Blockbuster.gsonimpl;

import com.google.gson.annotations.Since;

import java.util.concurrent.ConcurrentHashMap;

/**
 * This class represent an array&Hashmap of user object's for the json update conversion.
 * data member :
 * 		users := an array of movie object's.
 * 		usersHash := database for all the active movies that the server contain.
 */
public class users {
    @Since(2) private ConcurrentHashMap<String,user> usersHash;
    @Since(1) private user[] users;

    /**
     * default constructor.
     */
    public users(){
        usersHash = new ConcurrentHashMap<String, user>();
    }

    /**
     * add a user to the active database.
     * @param user
     * @return boolean if the user was been added to the database.
     */
    public boolean adduser(user user){
        boolean exist = usersHash.containsKey(user.getUsername());
        if(!exist)
            usersHash.put(user.getUsername(),user);
        return !exist;
    }
    
    /**
     * convert an array of users to hash map.
     */
    public void updateusersJson() {//for the update users Hash after Json
        for (int i = 0; i < users.length; i++) {
            adduser(users[i]);
        }
    }
    
    /**
     * convert an hashmap of users to array.
     */
    public void makeusersarrayforjson(){//make the Users ready to json file
        user [] ans = new user[usersHash.size()];
        int i = 0;
        for (String users:usersHash.keySet()) {
            ans[i] = usersHash.get(users);
            i++;
        }
        users = ans;
    }
    
    /**
     * check if the user exist with this password.
     * @param username
     * @param password
     * @return true if the user can login with the password.
     */
    public boolean exist(String username, String password) {
    	boolean ans = false;
        user check = this.usersHash.get(username);
        if (check != null && check.PasswordCorrect(password)) {
        	ans = true;
        }
        return ans;
    }
    
    /**
     * check if username is admin in the system.
     * @param username
     * @return true if the username is admin.
     */
    public boolean isTheUserAdmin(String username) {
    	boolean ans = false;
    	user check = this.usersHash.get(username);
    	if (check != null && check.getType()=="admin") {
    		ans = true;
    	}
    	return ans;
    }

    public user GetUser(String UserName){
        return usersHash.get(UserName);
    }
    /**
     * End Of File.
     */
}