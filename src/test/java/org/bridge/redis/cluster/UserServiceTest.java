package org.bridge.redis.cluster;

import org.apache.log4j.Logger;
import org.bridge.redis.bean.User;
import org.bridge.redis.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:spring-jedis.xml"})
public class UserServiceTest {

	private static final Logger logger = Logger.getLogger(UserServiceTest.class);
	
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
		
		logger.info("TEST OVER !");

	}
	
	
	@SuppressWarnings("resource")
	public static void main( String[] args ){
		
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:spring-jedis.xml");
		UserService userService =  (UserService) applicationContext.getBean("userService");
			
		User user1 = new User("user1ID", "User 1");
		User user2 = new User("user2ID", "User 2");
		
		System.out.println("==== getting objects from redis ====");
		System.out.println("User is not in redis yet: " + userService.get(user1));
		System.out.println("User is not in redis yet: " + userService.get(user2));
		
		System.out.println("==== putting objects into redis ====");
		userService.put(user1);
		userService.put(user2);
		
		System.out.println("==== getting objects from redis ====");
		System.out.println("User should be in redis yet: " + userService.get(user1));
		System.out.println("User should be in redis yet: " + userService.get(user2));

		System.out.println("==== deleting objects from redis ====");
		userService.delete(user1);
		userService.delete(user2);
		
		System.out.println("==== getting objects from redis ====");
		System.out.println("User is not in redis yet: " + userService.get(user1));
		System.out.println("User is not in redis yet: " + userService.get(user2));

	}

}
