package com.opsync.springsecuritytutorial.service;

import com.opsync.springsecuritytutorial.entity.User;
import com.opsync.springsecuritytutorial.model.UserModel;

public interface UserService {

	User registerUser(UserModel userModel);

	void saveVerificationTokenForUser(String token, User user);

	String validateVerificationToken(String token);

}
