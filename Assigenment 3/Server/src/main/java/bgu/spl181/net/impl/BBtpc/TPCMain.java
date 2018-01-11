package bgu.spl181.net.impl.BBtpc;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import bgu.spl181.net.impl.Blockbuster.BidiMessagingProtocolimpl;
import bgu.spl181.net.impl.Blockbuster.DataBaseHandler;
import bgu.spl181.net.impl.Blockbuster.LineMessageEncoderDecoder;
import bgu.spl181.net.srv.Server;

public class TPCMain {
    public static void main(String[] args) {
        String path = System.getProperty("user.dir");
        String userpath = path + "//Database//Users.json";
        String moviespath = path + "//Database//Movies.json";
        DataBaseHandler dataBaseHandler = new DataBaseHandler(new ReentrantReadWriteLock()
                ,new ReentrantReadWriteLock()
                ,moviespath
                ,userpath); //one shared object

        Server.threadPerClient(
        		Integer.parseInt(args[0]), //port
                () ->  new BidiMessagingProtocolimpl(dataBaseHandler), //protocol factory
                LineMessageEncoderDecoder::new //message encoder decoder factory
        ).serve();
    }
}
