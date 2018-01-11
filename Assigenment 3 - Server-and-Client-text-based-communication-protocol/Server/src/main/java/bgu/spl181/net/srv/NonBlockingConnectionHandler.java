package bgu.spl181.net.srv;

import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.api.MessageEncoderDecoder;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This class is an implementaion of ConnectionHandler which allow to process 
 * the client request by a thread from the ThreadPool. 
 * data members :
 * 		reactor : the main reactor for changing the socket channel possible income (W/R).
 * 		protocol : BidiMessagingProtocol for processing the client request via the current implementation of the server.
 * 		encdec : decode the bytes from the socketChannel.
 * 		writeQueue : database that contain the server response to the client. 
 * 		chan : the current socketChannel for this client.
 * @param <T> - generic implementation, i.e : String, boolean etc'.
 */
public class NonBlockingConnectionHandler<T> implements ConnectionHandler<T> {

    private static final int BUFFER_ALLOCATION_SIZE = 1 << 13; //8k
    private static final ConcurrentLinkedQueue<ByteBuffer> BUFFER_POOL = new ConcurrentLinkedQueue<>();
    private final BidiMessagingProtocol<T> protocol;
    private final MessageEncoderDecoder<T> encdec;
    private final Queue<ByteBuffer> writeQueue = new ConcurrentLinkedQueue<>();
    private final SocketChannel chan;
    private final Reactor reactor;

    /**
     * default constructor.
     * @param reader
     * @param protocol
     * @param chan
     * @param reactor
     */
    public NonBlockingConnectionHandler(
            MessageEncoderDecoder<T> reader,
            BidiMessagingProtocol<T> protocol,
            SocketChannel chan,
            Reactor reactor) {
        this.chan = chan;
        this.encdec = reader;
        this.protocol = protocol;
        this.reactor = reactor;
    }

    /**
     * this method generate a callback that will be called by a thread from
     *  the Threadpool (if the implementation is multi-Thread).
     * @return callback which will be processing the client request via the protocol implementation.
     */
    public Runnable continueRead() {
        ByteBuffer buf = leaseBuffer();

        boolean success = false;
        try {
            success = chan.read(buf) != -1;
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (success) {
            buf.flip();
            return () -> {
                try {
                    while (buf.hasRemaining()) {
                        T nextMessagePrep = encdec.decodeNextByte(buf.get());
                        if (nextMessagePrep != null) {
//                        	T nextMessage = (T) encdec.stringToArray(nextMessagePrep);
                        	protocol.process(nextMessagePrep);
//                            if (response != null) {
//                                writeQueue.add(ByteBuffer.wrap(encdec.encode(response)));
//                                reactor.updateInterestedOps(chan, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
//                            }
                        }
                    }
                } finally {
                    releaseBuffer(buf);
                }
            };
        } else {
            releaseBuffer(buf);
            close();
            return null;
        }

    }

    public void close() {
        try {
            chan.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean isClosed() {
        return !chan.isOpen();
    }

    public void continueWrite() {
        while (!writeQueue.isEmpty()) {
            try {
                ByteBuffer top = writeQueue.peek();
                chan.write(top);
                if (top.hasRemaining()) {
                    return;
                } else {
                    writeQueue.remove();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                close();
            }
        }

        if (writeQueue.isEmpty()) {
            if (protocol.shouldTerminate()) close();
            else reactor.updateInterestedOps(chan, SelectionKey.OP_READ);
        }
    }

    private static ByteBuffer leaseBuffer() {
        ByteBuffer buff = BUFFER_POOL.poll();
        if (buff == null) {
            return ByteBuffer.allocateDirect(BUFFER_ALLOCATION_SIZE);
        }

        buff.clear();
        return buff;
    }

    private static void releaseBuffer(ByteBuffer buff) {
        BUFFER_POOL.add(buff);
    }

    public BidiMessagingProtocol<T> getProtocol(){
        return protocol;
    }
    
	@Override
	public void send(T msg) {
        writeQueue.add(ByteBuffer.wrap(encdec.encode(msg)));
        reactor.updateInterestedOps(chan, SelectionKey.OP_READ | SelectionKey.OP_WRITE)	;
	}

	/**
	 * End of File.
	 */
}
