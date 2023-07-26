package com.soninisha.ums.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soninisha.ums.entity.User;
import com.soninisha.ums.rrepository.userRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private userRepository repository;

	@Override
	public List<User> getAllUser() {
	
		return repository.findAll();
	}

	@Override
	public User loadUserById(int id) {
		
		return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " Not Found"));
	}

	@Override
	public User createOrUpdateUser(User user) {
	
		return repository.save(user);
	}

	@Override
	public void removeUser(Integer id) {
		repository.deleteById(id);
		
	}

	@Override
	public User getUserByEmailandPassword(String email, String password) {
		return repository.findByEmailAndPassword(email , password);
	}

	@Override
	public boolean checkEmailExist(String email) {
		return repository.existsByEmail(email);
	}

	@Override
	public User getUserByEmail(String email) {
		return repository.findByEmail(email);
	}

}
