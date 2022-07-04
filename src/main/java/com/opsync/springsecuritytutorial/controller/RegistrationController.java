package com.opsync.springsecuritytutorial.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.opsync.springsecuritytutorial.entity.User;
import com.opsync.springsecuritytutorial.event.RegistrationCompleteEvent;
import com.opsync.springsecuritytutorial.model.UserModel;
import com.opsync.springsecuritytutorial.service.UserService;

@RestController
public class RegistrationController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@PostMapping("/register")
	public String registerUser(@RequestBody UserModel userModel, 
			final HttpServletRequest request) {
		User user = userService.registerUser(userModel);
		publisher.publishEvent(
				new RegistrationCompleteEvent(user, applicationUrl(request)));
		
		return "Success";
	}
	
	@GetMapping("/verifyRegistration")
	public String verifyRegistration(@RequestParam("token") String token) {
		String result = userService.validateVerificationToken(token);
		if(result.equalsIgnoreCase("valid")) {
			return "User Verified Successfully";
		}
		
		return "Bad user";
	}

	private String applicationUrl(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return "http://" +
				request.getServerName() + 
				":" +
				request.getServerPort()+
				request.getContextPath();
	}
}
