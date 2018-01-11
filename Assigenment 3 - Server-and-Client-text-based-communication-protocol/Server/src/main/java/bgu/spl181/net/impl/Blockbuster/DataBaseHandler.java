package bgu.spl181.net.impl.Blockbuster;

import java.util.concurrent.locks.ReadWriteLock;
/**
 * 
 * this class is aid class for W/R the json database files with locks 
 * (solving the sync in multi-thread implementation).
 * data member :
 *  	ReadWriteLockMovie - ReadWriteLock of the movie json file.
 *		readWriteLockUsers - ReadWriteLock of the user json file.
 *		PathUsers - the current path of the database file that contain the users data.
 *		pathMovie - the current path of the database file that contain the users data.
 */
public class DataBaseHandler {
    private ReadWriteLock ReadWriteLockMovie;
    private ReadWriteLock readWriteLockUsers;
    private String PathUsers;
    private String pathMovie;

    /**
     * default constructor.
     * @param ReadWriteLockMovie
     * @param readWriteLockUsers
     * @param PathUsers
     * @param pathMovie
     */
    public DataBaseHandler(ReadWriteLock ReadWriteLockMovie, ReadWriteLock readWriteLockUsers,
                           String PathUsers,String pathMovie){
        this.ReadWriteLockMovie = ReadWriteLockMovie;
        this.readWriteLockUsers = readWriteLockUsers;
        this.PathUsers = PathUsers;
        this.pathMovie = pathMovie;

    }
    /**
     * getter for the ReadWriteLockMovie data member.
     * @return ReadWriteLockMovie
     */
    public ReadWriteLock getReadWriteLockMovie() {
        return ReadWriteLockMovie;
    }

    /**
     * getter for the ReadWriteLockMovie data member.
     * @return getReadWriteLockUsers
     */
    public ReadWriteLock getReadWriteLockUsers() {
        return readWriteLockUsers;
    }

    /**
     * getter for the PathUsers data member.
     * @return PathUsers
     */
    public String getPathUsers() {
        return PathUsers;
    }

    /**
     * getter for the pathMovie data member.
     * @return pathMovie
     */
    public String getPathMovie() {
        return pathMovie;
    }
    
    /**
     * End of file.
     */
}
