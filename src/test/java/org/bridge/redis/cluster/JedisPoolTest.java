package org.bridge.redis.cluster;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:spring-jedis-cluster.xml"})
public class JedisPoolTest {
	
	@Autowired
	private JedisPool jedisPool;
	
	@Test
    public void objectOpTest(){
    	Jedis jedis = jedisPool.getResource();
    	
    	jedis.set("person.001.name", "frank");
    	jedis.set("person.001.city", "beijing");
    	String name = jedis.get("person.001.name");
    	String city = jedis.get("person.001.city");
    	
    	assertEquals("frank",name);
    	assertEquals("beijing",city);
    	
    	jedis.del("person.001.name");
    	boolean result = jedis.exists("person.001.name");
    	
    	assertEquals(false,result); 
    	result = jedis.exists("person.001.city");
    	assertEquals(true,result);
    	
    	//jedis的close 方法仅仅是将jedis连接返还会pool中，并没有真正close。
    	jedis.close();
    	
    	jedis = jedisPool.getResource();
    	assertEquals(false, jedis==null);
    	result = jedis.exists("person.001.city");
    	assertEquals(true,result);
    	
    	jedis.close();
    	
    }

}
