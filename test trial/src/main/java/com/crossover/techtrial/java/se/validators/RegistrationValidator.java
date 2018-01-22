package com.crossover.techtrial.java.se.validators;

import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.crossover.techtrial.java.se.objects.UserRegistration;

/**
 * This class is used to validate data sent by the user while registering
 * @author
 *
 */
public class RegistrationValidator implements Validator{

	private final static Pattern EMAIL_PATTERN = Pattern.compile(".+@.+\\.[a-z]+");
	
	@Override
	public boolean supports(Class<?> clazz) {
		return  UserRegistration.class.equals(clazz);
	}
	
	private boolean isEmail(String value) {
		return EMAIL_PATTERN.matcher(value).matches();
    }

	@Override
	public void validate(Object target, Errors errors) {
		UserRegistration userRegistration=(UserRegistration) target;
		//reject if email is empty
		if(userRegistration.getEmail()==null || userRegistration.getEmail().equals("")){
			errors.rejectValue("email", "","email.required");
		}
		//reject if password is empty
		if(userRegistration.getPassword()==null || userRegistration.getPassword().equals("")){
			errors.rejectValue("password", "","password.required");
		}
		
		if(userRegistration.getEmail()!=null){			
			//reject if email is invalide
			if (!isEmail(userRegistration.getEmail())) {
			   errors.rejectValue("email", "","email.invalide");
		    }
		}
		
		if(userRegistration.getPassword()!=null){
			//Reject if the size of the password is not between 5 and 30
			if(userRegistration.getPassword().length()<5 || userRegistration.getPassword().length()>30){
			   errors.rejectValue("password", "","password.invalide");
			}
			//reject if the passwords do not match
			if(!userRegistration.getPassword().equals(userRegistration.getConfirmPassword())){
	            errors.rejectValue("password","", "password.match");
	        }
		}
	}
}
