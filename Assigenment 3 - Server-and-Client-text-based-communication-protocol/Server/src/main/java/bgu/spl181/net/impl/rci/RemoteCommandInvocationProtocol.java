package bgu.spl181.net.impl.rci;

import bgu.spl181.net.api.MessagingProtocol;
import java.io.Serializable;

public class RemoteCommandInvocationProtocol<T> implements MessagingProtocol<Serializable> {

    private T arg;

    public RemoteCommandInvocationProtocol(T arg) {
        this.arg = arg;
    }

    @Override
    public Serializable process(Serializable msg) {
        return ((Command) msg).execute(arg);
    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }

}
