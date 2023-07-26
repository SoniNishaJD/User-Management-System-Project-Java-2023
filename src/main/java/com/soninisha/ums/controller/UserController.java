package com.soninisha.ums.controller;

import static com.soninisha.ums.constant.JWTUtil.EMAIL_EXIST;
import static com.soninisha.ums.constant.JWTUtil.REGISTOR;
import static com.soninisha.ums.constant.JWTUtil.ROLE;

import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.soninisha.ums.entity.User;
import com.soninisha.ums.service.UserService;
import com.soninisha.ums.util.Helper;

import jakarta.servlet.http.HttpSession;


@Controller
public class UserController {
	
	private static final Object UPDATE_PROFILE = null;
	private static final Object CHANGE_PASSWORD = null;
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
		Random random = new Random();   
		int y = random.nextInt(800000)+100000;
		user.setVcode(""+y);
		
		
		session.setAttribute("msg",REGISTOR);
		service.createOrUpdateUser(user);
		}
		
		
		return "login";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@PostMapping("/login")
	public String CheckLogin(@RequestParam("email") String email, @RequestParam("password") String password,
			HttpSession session) {
		
		User user = service.getUserByEmailandPassword(email, password);
		if (user != null) {
			session.setAttribute("uname", user.getFirstName() + " " + user.getLastName());
			session.setAttribute("uid", user.getId());
			session.setAttribute("urole", user.getRole());
			System.out.println("xxxxxxxxxxxxx");
			System.out.println(user);
			System.out.println("xxxxxxxxxxxxx");
			
			if (Helper.checkUserRole()) {
				session.setAttribute("msg", "You are successfully Login..");
				return "/dashboard";
			} else {
				session.setAttribute("msg", "Something went Wrong.");
				return "redirect:/logout";
			}

		} else

		{
			session.setAttribute("msg", "Wrong Username or Password...");
			return "redirect:/login";
		}

		}
	@GetMapping("/profile")
	public String profile(Model m) {
		
		if(!Helper.checkUserRole()) {
			return "redirect:/logout";
		}
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession();
		int uid = 0;
		if(session.getAttribute("uid") != null) {
			uid = (int)session.getAttribute("uid");
		} 
		User user = service.loadUserById(uid);
		m.addAttribute("user",user);
		
		
		return "profile";
	}
	
	@GetMapping("/editProfile")
	public String editProfile(Model m) {
		
		if(!Helper.checkUserRole()) {
			return "redirect:/logout";
		}
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession();
		int uid = 0;
		if(session.getAttribute("uid") != null) {
			uid = (int)session.getAttribute("uid");
		} 
		User user = service.loadUserById(uid);
		m.addAttribute("user",user);
		
		
		return "editProfile"; //view page name
	}
	
	@PostMapping("/editProfile")
	public String updateProfile(User user) {
		if(!Helper.checkUserRole()) {
			return "redirect:/admin/logout";
		}
		
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession();
		int uid = 0;
		if(session.getAttribute("uid") != null) {
			uid = (int)session.getAttribute("uid");
			User user2 = service.loadUserById(uid);
			user2.setFirstName(user.getFirstName());
			user2.setLastName(user.getLastName());
			user2.setEmail(user.getEmail());
			user2.setPhone(user.getPhone());
			service.createOrUpdateUser(user2);
		} 
		
		session.setAttribute("msg", UPDATE_PROFILE);
		
		return "profile";
	}
	
	@GetMapping("/changepassword")
	public String changepassword1(@RequestParam(required=false, name="password")String password) {
//		if(!Helper.checkUserRole()) {
//			return "redirect:/logout";
//		}
		return "/changepassword";
	}
	@PostMapping("/changepassword")
	public String changepassword2(@RequestParam(required=false, name="password")String password) {
		if(!Helper.checkUserRole()) {
			return "redirect:/logout";
		}
		
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession();
		int uid = 0;
		if(session.getAttribute("uid") != null) {
			uid = (int)session.getAttribute("uid");
			User user = service.loadUserById(uid);
			user.setPassword(password);
			service.createOrUpdateUser(user);
		} 
		
		session.setAttribute("msg",CHANGE_PASSWORD);
		
		return "redirect:/profile";
	}
	

	@GetMapping("/forgotpassword")
	public String forgotpassword() {		
		
		return "/forgotpassword";
		
	}
	
	@PostMapping("/forgotpassword")
	public String forgotpassword1(@RequestParam("email") String email, Model m) {
		if(service.checkEmailExist(email)) {
			User user = service.getUserByEmail(email);
			m.addAttribute("uid",user.getId());
			return "/resetPassword";
		}else {
		
		return "/forgotpassword";
		}
	}
	
	
	
	@PostMapping("/resetPassword")
	public String resetpassword1(@RequestParam("id")int id, @RequestParam("password")String password, @RequestParam("con_password")String con_password) {	
		
		if(password.equals(con_password))
		{
		User user = service.loadUserById(id);
		user.setPassword(password);
		service.createOrUpdateUser(user);
		
		}else {
		 
		}
		return "redirect:/";
	}
	
	@GetMapping("/logout")
	public String logout() {

		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession();

		if (session.getAttribute("uname") != null)
			session.removeAttribute("uname");

		if (session.getAttribute("uid") != null)
			session.removeAttribute("uid");

		if (session.getAttribute("urole") != null)
			session.removeAttribute("urole");

		return "redirect:/login";
	}
	
	
}
