package org.bridge.redis.pubsub;

import java.io.Serializable;
import java.util.Map;

public interface MessageDelegate {
	
	public void handleMessage(String message);
	 
    public void handleMessage(Map<?, ?> message);
 
    public void handleMessage(byte[] message);
 
    public void handleMessage(Serializable message);
 
    // pass the channel/pattern as well
    public void handleMessage(Serializable message, String channel);

}
