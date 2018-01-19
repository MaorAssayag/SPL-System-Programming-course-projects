package bgu.spl181.net.impl.Blockbuster;

import bgu.spl181.net.srv.Server;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BlockbusterServer {
    public static void main(String[] args) {

        DataBaseHandler dataBaseHandler = new DataBaseHandler(new ReentrantReadWriteLock()
                ,new ReentrantReadWriteLock(),
        "C:\\Users\\MaorA\\Desktop\\spl\\SPL-System-Programming-course-projects\\Assigenment 3 - Server-and-Client-text-based-communication-protocol\\Server\\Database\\Users.json"
        ,"C:\\Users\\MaorA\\Desktop\\spl\\SPL-System-Programming-course-projects\\Assigenment 3 - Server-and-Client-text-based-communication-protocol\\Server\\Database\\Movies.json"); //one shared object

// you can use any server...
        Server.threadPerClient(
                7777, //port
                () -> new BidiMessagingProtocolimpl(dataBaseHandler), //protocol factory
                LineMessageEncoderDecoder::new //message encoder decoder factory
        ).serve();
//
//        Server.reactor(
//                Runtime.getRuntime().availableProcessors(),
//                7777, //port
//                () ->  new BidiMessagingProtocolimpl(dataBaseHandler), //protocol factory
//                LineMessageEncoderDecoder::new //message encoder decoder factory
//        ).serve();

    }
}
