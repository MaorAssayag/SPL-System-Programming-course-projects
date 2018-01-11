package bgu.spl181.net.impl.newsfeed;

import bgu.spl181.net.impl.rci.Command;
import java.io.Serializable;

public class PublishNewsCommand implements Command<NewsFeed> {
 
    private String channel;
    private String news;
 
    public PublishNewsCommand(String channel, String news) {
        this.channel = channel;
        this.news = news;
    }
 
    @Override
    public Serializable execute(NewsFeed feed) {
        feed.publish(channel, news);
        return "OK";
    }
 
}