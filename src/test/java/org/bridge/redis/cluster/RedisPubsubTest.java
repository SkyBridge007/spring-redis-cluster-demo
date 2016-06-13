package org.bridge.redis.cluster;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:spring-redis-pubsub.xml"})
public class RedisPubsubTest {
	
	@Autowired
	private RedisMessageListenerContainer redisContainer;
	
	@Test
    public void redisPubSubTest() throws InterruptedException {
       
        while (true) {
            if (redisContainer.isRunning()) {
                System.out.println("RedisMessageListenerContainer is running..");
            }
            Thread.sleep(5000);
        }
    }

}
