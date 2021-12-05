package com.example.pantryparserbackend.Passwords;

import com.example.pantryparserbackend.Requests.OTPRequest;
import com.example.pantryparserbackend.Requests.PasswordResetRequest;
import com.example.pantryparserbackend.Users.User;
import com.example.pantryparserbackend.Users.UserRepository;
import com.example.pantryparserbackend.Services.EmailService;
import com.example.pantryparserbackend.Utils.MessageUtil;
import com.example.pantryparserbackend.Services.PasswordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Api(value = "Password Controller", description = "Controller for password and OTP functionality")
public class PasswordController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordService passwordService;
	@Autowired
	private EmailService emailService;

	/**
	 * Sends an OTP for email verification to the provided user
	 * @param user_id input user
	 * @return successful verification or failure message
	 */
	@ApiOperation(value = "Sends an OTP for verifying an email address")
	@GetMapping(path = "/user/{user_id}/verify-email")
	public String sendVerifyOTP(@PathVariable int user_id) {
		User user = userRepository.findById(user_id);
		if (user == null) {
			return MessageUtil.newResponseMessage(false, "user not found");
		}

		return emailService.sendOTPEmail("VerifyEmail", user);
	}

	/**
	 * Verifies the email of the provided user
	 * @param user_id input user
	 * @param OTP input OTP
	 * @return successful verification or failure message
	 */
	@ApiOperation(value = "Takes in an OTP and determines if it is valid. If so, verifies email")
	@PostMapping(path = "/user/{user_id}/verify-email")
	public String verifyEmail(@PathVariable int user_id, @RequestBody OTPRequest OTP) {
		User user = userRepository.findById(user_id);

		if(user == null) {
			return MessageUtil.newResponseMessage(false, "user was not found");
		}
		if(passwordService.useOTP(OTP.OTP, user)) {
			user.setEmail_verified(true);
			userRepository.save(user);
			return MessageUtil.newResponseMessage(true, "successfully changed password");
		}
		return MessageUtil.newResponseMessage(false, "invalid OTP, please try again or try to get a new OTP");
	}

	/**
	 * A password reset route that takes in an email to find the user
	 * @param email email of the user
	 * @return string message success or failure
	 */
	@ApiOperation(value = "Sends an OTP for resetting a user's password")
	@GetMapping(path = "/user/email/{email}/password-reset")
	public String sendResetOTP(@PathVariable String email) {
		User user = userRepository.findByEmail(email);
		if (user == null){
			return MessageUtil.newResponseMessage(false, "account not found");
		}

		return emailService.sendOTPEmail("PasswordReset", user);
	}

	/**
	 * Route that performs a password reset by a user's email
	 * @param email the email of the user
	 * @param request the inputted values in the request
	 * @return string success or fail
	 */
	@ApiOperation(value = "Changes the user's password if they send in a valid OTP")
	@PostMapping(path = "/user/email/{email}/password-reset")
	public String resetPassword(@PathVariable String email, @RequestBody PasswordResetRequest request) {
		User user = userRepository.findByEmail(email);
		if (user == null){
			return MessageUtil.newResponseMessage(false, "account not found");
		}
		return this.changePassword(user.getId(), request);
	}

	/**
	 * a route for simply changing a user's password
	 * @param user_id id of the user
	 * @return string success or fail
	 */
	@ApiOperation(value = "Sends an OTP for changing a user's password")
	@GetMapping(path = "/user/{user_id}/password-change")
	public String sendChangeOTP(@PathVariable int user_id) {
		User user = userRepository.findById(user_id);
		if (user == null){
			return MessageUtil.newResponseMessage(false, "user not found");
		}

		return emailService.sendOTPEmail("PasswordChange", user);
	}

	/**
	 * A route that verifies the OTP then changes the password
	 * @param user_id id of the user
	 * @param request otp and new password
	 * @return string success or fail
	 */
	@ApiOperation(value = "Changes a user's password if they send in a valid OTP")
	@PostMapping(path = "/user/{user_id}/password-change")
	public String changePassword(@PathVariable int user_id, @RequestBody PasswordResetRequest request) {
		User user = userRepository.findById(user_id);
		if(user == null) {
			return MessageUtil.newResponseMessage(false, "user was not found");
		}
		if(passwordService.useOTP(request.OTP, user)) {
			user.setPassword(request.newPassword);
			userRepository.save(user);
			return MessageUtil.newResponseMessage(true, "successfully changed password");
		}
		return MessageUtil.newResponseMessage(false, "invalid OTP, please try again");
	}
}
