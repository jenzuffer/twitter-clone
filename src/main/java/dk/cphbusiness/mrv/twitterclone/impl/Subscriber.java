package dk.cphbusiness.mrv.twitterclone.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;

public class Subscriber extends JedisPubSub {
    private static Logger logger = LoggerFactory.getLogger(Subscriber.class);

    public Subscriber() {

    }

    @Override
    public void onMessage(String channel, String message) {
        logger.info("Message received. Channel: {}, Msg: {}", channel, message);

    }


    @Override
    public void onPMessage(String pattern, String channel, String message) {

    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        logger.info("Client is Subscribed to channel : " + channel);
        logger.info("Client is Subscribed to " + subscribedChannels + " no. of channels");


    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        System.out.println("Client is Unsubscribed from channel : " + channel);
        System.out.println("Client is Subscribed to " + subscribedChannels + " no. of channels");
    }

    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {

    }

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {

    }
}
