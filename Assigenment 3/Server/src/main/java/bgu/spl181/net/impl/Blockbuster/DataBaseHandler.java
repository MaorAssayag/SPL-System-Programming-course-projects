package bgu.spl181.net.impl.Blockbuster;

import java.util.concurrent.locks.ReadWriteLock;

public class DataBaseHandler {
    private ReadWriteLock ReadWriteLock;

    public DataBaseHandler(ReadWriteLock readWriteLock){
        this.ReadWriteLock = readWriteLock;
    }

    public ReadWriteLock getReadWriteLock() {
        return ReadWriteLock;
    }
}
