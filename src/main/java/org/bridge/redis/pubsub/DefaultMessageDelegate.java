package org.bridge.redis.pubsub;

import java.io.Serializable;
import java.util.Map;

public class DefaultMessageDelegate implements MessageDelegate {

	@Override
    public void handleMessage(String message) {
        System.out.println("handleMessage(String message):" + message);
    }
 
    @Override
    public void handleMessage(Map<?, ?> message) {
        System.out.println("handleMessage(Map<?, ?> message):" + message);
    }
 
    @Override
    public void handleMessage(byte[] message) {
        System.out.println("handleMessage(byte[] message):"
                + new String(message));
    }
 
    @Override
    public void handleMessage(Serializable message) {
        System.out.println("handleMessage(Serializable message):"
                + message.toString());
    }
 
    @Override
    public void handleMessage(Serializable message, String channel) {
        System.out
                .println("handleMessage(Serializable message, String channel):"
                        + message.toString() + ", channel:" + channel);
    }

}
