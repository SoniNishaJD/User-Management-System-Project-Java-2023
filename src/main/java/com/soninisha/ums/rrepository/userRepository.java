package com.soninisha.ums.rrepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soninisha.ums.entity.User;

public interface userRepository  extends JpaRepository<User, Integer>{
	
	User findByEmailAndPassword(String email, String password);

	boolean existsByEmail(String email);
	
	User findByEmail(String email);

}
