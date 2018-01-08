package bgu.spl181.net.impl.Blockbuster.gsonimpl;

/**
 * aid class for the user and movie connection in the json file.
 * @author MaorA
 *
 */
public class UserMovie {
    private String id;
    private String name;

    public UserMovie (String id,String name){
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return Integer.valueOf(id);
    }
    
    /**
     * End of File.
     */
}
