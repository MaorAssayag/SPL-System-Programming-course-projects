package bgu.spl181.net.impl.newsfeed;

import bgu.spl181.net.impl.rci.Command;
import java.io.Serializable;

public class FetchNewsCommand implements Command<NewsFeed> {

    private String channel;

    public FetchNewsCommand(String channel) {
        this.channel = channel;
    }

    @Override
    public Serializable execute(NewsFeed feed) {
        return feed.fetch(channel);
    }

}
