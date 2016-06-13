package org.bridge.redis.cluster;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import junit.framework.TestCase;
import redis.clients.jedis.JedisCluster;

/**
 * Unit test for simple App.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:spring-jedis-cluster.xml"})
public class JedisClusterTest extends TestCase{
    
	@Autowired
	private JedisCluster jedisCluster;
    
    @Test 
    public void basicOpTestForCluster(){
    	
    	long begin = System.currentTimeMillis();
    	for(int i=0;i<10000; i++){
    		jedisCluster.set("person." + i + ".name", "frank");
    		jedisCluster.set("person." + i + ".city", "beijing");
    		String name = jedisCluster.get("person." + i + ".name");
    		String city = jedisCluster.get("person." + i + ".city");
    		assertEquals("frank",name); assertEquals("beijing",city);
    		jedisCluster.del("person." + i + ".name");
    		boolean result = jedisCluster.exists("person." + i + ".name");
    		assertEquals(false,result);
    		result = jedisCluster.exists("person." + i + ".city");
    		assertEquals(true, result);
    	}
    	long end = System.currentTimeMillis();
    	System.out.println("total time: " + (end-begin)/1000);
    }
    
    
}
