package org.bridge.redis.service;

import org.bridge.redis.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service("userSerivce")
public class UserService {

	@Autowired(required=true)
	RedisTemplate<String, User> redisTemplate;
	
	/**
	 * org.springframework.data.redis.core.HashOperations.put(String key, Object hashKey, Object value);
	 * 上述方法的三个参数，可以分别理解为：key--value的对象类型标示，hashKey--value的真实键值，value--实际存放的对象
	 * @param user
	 */
	public void put(User user){
		redisTemplate.opsForHash().put(user.getObjectKey(), user.getKey(), user);
	}
	
	public void delete(User key){
		redisTemplate.opsForHash().delete(key.getObjectKey(), key.getKey());
	}
	
	public User get(User key){
		return (User)redisTemplate.opsForHash().get(key.getObjectKey(), key.getKey());
	}

	public RedisTemplate<String, User> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, User> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	
}
