package com.soninisha.ums.controller;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.soninisha.ums.entity.User;
import com.soninisha.ums.service.UserService;

import jakarta.servlet.http.HttpSession;
import static com.soninisha.ums.constant.JWTUtil.EMAIL_EXIST;
import static com.soninisha.ums.constant.JWTUtil.ROLE;
import static com.soninisha.ums.constant.JWTUtil.REGISTOR;


@Controller
public class UserController {
	
	private UserService service;

	public UserController(UserService service) {
		super();
		this.service = service;
	}
	
	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	@GetMapping("/register")
	public String registration() {
		return "registration";
	}
	
	@PostMapping("/register")
	public String registration(@ModelAttribute User user , HttpSession session) {
		if(service.checkEmailExist(user.getEmail()))
		{
			session.setAttribute("msg", EMAIL_EXIST);
			
		}else {
		user.setRole(ROLE);
		user.setRegDate(new Date());
		user.setVcode("1234");
		
		
		session.setAttribute("msg",REGISTOR);
		service.createOrUpdateUser(user);
		}
		
		
		return "login";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	
	

}
