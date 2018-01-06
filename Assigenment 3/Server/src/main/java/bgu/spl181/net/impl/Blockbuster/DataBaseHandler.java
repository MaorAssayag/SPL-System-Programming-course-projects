package bgu.spl181.net.impl.Blockbuster;

import java.util.concurrent.locks.ReadWriteLock;

public class DataBaseHandler {
    private ReadWriteLock ReadWriteLock;
    private String Path;

    public DataBaseHandler(ReadWriteLock readWriteLock, String Path){
        this.ReadWriteLock = readWriteLock;
        this.Path = Path;
    }

    public ReadWriteLock getReadWriteLock() {
        return ReadWriteLock;
    }
}
