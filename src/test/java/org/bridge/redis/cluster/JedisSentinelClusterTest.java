package org.bridge.redis.cluster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.bridge.redis.bean.User;
import org.bridge.redis.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.data.redis.support.collections.RedisList;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:spring-redis-sentinel-cluster.xml"})
public class JedisSentinelClusterTest {
	
	private static Logger logger = Logger.getLogger(JedisSentinelClusterTest.class);
	
	@SuppressWarnings("rawtypes")
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	private JedisConnectionFactory jedisConnectionFactory;
	/*
	@Autowired
	private RedisList<String> springList;
	*/
	// 测试RedisTemplate,自主处理key的可读性(String序列号)
	@SuppressWarnings("unchecked")
	@Test 
    public void redisTemplateTestForCluster(){
		
		String key = "spring";
	    ListOperations<String, String> lop = redisTemplate.opsForList();
	    RedisSerializer<String> serializer = new StringRedisSerializer();
	    redisTemplate.setKeySerializer(serializer);
	    redisTemplate.setValueSerializer(serializer);
	    // rt.setDefaultSerializer(serializer);

	    lop.leftPush(key, "aaa");
	    lop.leftPush(key, "bbb");
	    long size = lop.size(key); // rt.boundListOps(key).size();
	    Assert.assertEquals(2, size);
    	
    	
    }
	
	// 测试便捷对象StringRedisTemplate
	  @Test
	  public void stringRedisTemplateTest() {
	    ValueOperations<String, String> vop = stringRedisTemplate.opsForValue();
	    String key = "string_redis_template";
	    String v = "use StringRedisTemplate set k v";
	    vop.set(key, v);
	    String value = vop.get(key);
	    Assert.assertEquals(v, value);
	  }
	  
	  // 测试Callback
	  @Test
	  public void test61() {
	    Long dbsize = (Long) stringRedisTemplate.execute(new RedisCallback<Object>() {
	          @Override
	          public Long doInRedis(RedisConnection connection)
	              throws DataAccessException {
	            StringRedisConnection stringRedisConnection=(StringRedisConnection)connection;
	            return stringRedisConnection.dbSize();
	          }
	        });
	    System.out.println("dbsize:" + dbsize);
	  }
	  
	  @SuppressWarnings("rawtypes") 
	  @Test
	  public void test62() {
	    List<Object> txresult = stringRedisTemplate.execute(new SessionCallback<List<Object>>() {
	        @SuppressWarnings("unchecked")
			@Override
	          public List<Object> execute(RedisOperations operations)
	              throws DataAccessException {
	            operations.multi();
	            operations.opsForHash().put("hkey", "multikey4",
	                "multivalue4");
	            operations.opsForHash().get("hkey", "k1");

	            return operations.exec();
	          }

	        });
	    for (Object o : txresult) {
	      System.out.println(o);
	      /**
	       * 0. false/true 
	       * 1. v1
	       */
	    }
	  }
	  
	  // 测试Lua脚本
	  @Test
	  public void test71() {
	    List<String> keys = new ArrayList<String>();
	    RedisScript<Long> script = new DefaultRedisScript<Long>(
	        "local size = redis.call('dbsize'); return size;", Long.class);
	    Long dbsize = stringRedisTemplate
	        .execute(script, keys, new Object[] {});
	    System.out.println("sha1:" + script.getSha1());
	    System.out.println("Lua:" + script.getScriptAsString());
	    System.out.println("dbsize:" + dbsize);
	    logger.info("dbsize:" + dbsize);
	  }
	  
	  @Test
	  public void test72() {
	    DefaultRedisScript<Boolean> script = new DefaultRedisScript<Boolean>();
	    /**
	     * isexistskey.lua内容如下：
	     * 
	     * return tonumber(redis.call("exists",KEYS[1])) == 1;
	     */
	    script.setScriptSource(new ResourceScriptSource(new ClassPathResource(
	        "isexistskey.lua")));

	    script.setResultType(Boolean.class);// Must Set

	    System.out.println("script:" + script.getScriptAsString());
	    Boolean isExist = stringRedisTemplate.execute(script,
	        Collections.singletonList("k2"), new Object[] {});
	    logger.info(isExist.toString());
	    Assert.assertTrue(isExist);
	  }
	  
	  
	  @Test
	  public void test8() {
	    RedisAtomicInteger rai = new RedisAtomicInteger("redis:atomic",jedisConnectionFactory);
	    System.out.println(rai.get());
	  }

	  // 测试Redis Collection
	  @SuppressWarnings({ "unchecked", "resource" })
	  @Test
	  public void test9() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-redis-sentinel-cluster.xml");
	    RedisList<String> redisList = (RedisList<String>) ctx
	            .getBean("springList");
	    redisList.clear();
	    redisList.addFirst("china");
	    redisList.add("in");
	    redisList.add("go");
	    redisList.addLast("made");
	    System.out.println(redisList.getKey());
	  }
	  
	  
	  @Autowired
	  private UserService userService;
	
	  @Test
	  public void UserRedisTest(){
		User user = new User("us123343","Bridge");
		userService.put(user);
		User user1 = userService.get(user);
		Assert.assertEquals(user, user1);
		
		userService.delete(user);
		user1 = userService.get(user);
		Assert.assertEquals(null, user1);
		logger.info("OVER!");
		
	  }

}
