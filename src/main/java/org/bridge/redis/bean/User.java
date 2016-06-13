package org.bridge.redis.bean;

import java.io.Serializable;

public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 694874190917349935L;
	
	public static final String OBJECT_KEY = "USER";
	
	private String id;
	
	private String name;
	
	public User(){
	}

	public User(String id){
		this.id = id;
	}
	
	public User(String id,String name){
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + "]";
	}
	
	public String getKey(){
		return getId();
	}
	
	public String getObjectKey(){
		return OBJECT_KEY;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		User user = (User) o;

		if (!id.equals(user.id)) return false;
		return name.equals(user.name);

	}

	@Override
	public int hashCode() {
		int result = id.hashCode();
		result = 31 * result + name.hashCode();
		return result;
	}
}
