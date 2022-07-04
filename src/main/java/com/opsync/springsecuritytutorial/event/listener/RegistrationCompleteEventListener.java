package com.opsync.springsecuritytutorial.event.listener;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.opsync.springsecuritytutorial.entity.User;
import com.opsync.springsecuritytutorial.event.RegistrationCompleteEvent;
import com.opsync.springsecuritytutorial.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent>{

	@Autowired
	private UserService userService;
	
	@Override
	public void onApplicationEvent(RegistrationCompleteEvent event) {
		//Create the Verification Token for the User with link
		User user = event.getUser();
		String token = UUID.randomUUID().toString();
		userService.saveVerificationTokenForUser(token, user);
		//Send mail to user
		String url = event.getApplicationUrl() + "/verifyRegistration?token="+ token;
		
		//send verification email()
		log.info("Click the link to verify your account: {}", url);
	}

}
