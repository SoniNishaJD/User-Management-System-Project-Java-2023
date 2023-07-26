package com.soninisha.ums.service;

import java.util.List;

import com.soninisha.ums.entity.User;

public interface UserService {

	List<User> getAllUser();

	User loadUserById(int id);

	User createOrUpdateUser(User user);

	void removeUser(Integer id);

	User getUserByEmailandPassword(String email, String password);
	
	User getUserByEmail(String email);

	boolean checkEmailExist(String email);
}
