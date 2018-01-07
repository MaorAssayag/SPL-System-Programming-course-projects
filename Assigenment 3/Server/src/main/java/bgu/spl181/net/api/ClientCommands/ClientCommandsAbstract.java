package bgu.spl181.net.api.ClientCommands;

import bgu.spl181.net.impl.Blockbuster.DataBaseHandler;

import java.util.concurrent.locks.ReadWriteLock;

public abstract class ClientCommandsAbstract {
    protected DataBaseHandler dataBaseHandler;
    protected String [] Commands;


    public abstract String execute();

}
