package bgu.spl181.net.impl.Blockbuster;

import java.util.concurrent.locks.ReadWriteLock;
// this
public class DataBaseHandler {
    private ReadWriteLock ReadWriteLockMovie;
    private ReadWriteLock readWriteLockUsers;
    private String PathUsers;
    private String pathMovie;

    public DataBaseHandler(ReadWriteLock ReadWriteLockMovie, ReadWriteLock readWriteLockUsers,
                           String PathUsers,String pathMovie){
        this.ReadWriteLockMovie = ReadWriteLockMovie;
        this.readWriteLockUsers = readWriteLockUsers;
        this.PathUsers = PathUsers;
        this.pathMovie = pathMovie;

    }

    public ReadWriteLock getReadWriteLockMovie() {
        return ReadWriteLockMovie;
    }

    public ReadWriteLock getReadWriteLockUsers() {
        return readWriteLockUsers;
    }

    public String getPathUsers() {
        return PathUsers;
    }

    public String getPathMovie() {
        return pathMovie;
    }
}
