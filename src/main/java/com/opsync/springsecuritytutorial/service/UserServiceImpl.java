package com.opsync.springsecuritytutorial.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.opsync.springsecuritytutorial.entity.User;
import com.opsync.springsecuritytutorial.entity.VerificationToken;
import com.opsync.springsecuritytutorial.model.UserModel;
import com.opsync.springsecuritytutorial.repository.UserRepository;
import com.opsync.springsecuritytutorial.repository.VerificationTokenRepository;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private VerificationTokenRepository verificationTokenRepository;
	
	@Override
	public User registerUser(UserModel userModel) {
		User user = new User();
		user.setEmail(userModel.getEmail());
		user.setFirstName(userModel.getFirstName());
		user.setLastName(userModel.getLastName());
		user.setRole("USER");
		user.setPassword(passwordEncoder.encode(userModel.getPassword()));
		
		userRepository.save(user);
		
		return user;
	}


	@Override
	public void saveVerificationTokenForUser(String token, User user) {
		VerificationToken verificationToken = new VerificationToken(user, token);
		verificationTokenRepository.save(verificationToken);
	}


	@Override
	public String validateVerificationToken(String token) {
		VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
		if(verificationToken == null) {
			return "invalid";
		}
		
		User user = verificationToken.getUser();
		Calendar cal = Calendar.getInstance();
		
		if((verificationToken.getExpirationTime().getTime()-cal.getTime().getTime()) <=0) {
			verificationTokenRepository.delete(verificationToken);
			return "expired";
		}
		
		user.setEnabled(true);
		userRepository.save(user);
		
		return "valid";
	}

}
