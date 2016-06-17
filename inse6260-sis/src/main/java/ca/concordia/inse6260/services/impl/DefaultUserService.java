package ca.concordia.inse6260.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.concordia.inse6260.dao.StudentDAO;
import ca.concordia.inse6260.dao.UserDAO;
import ca.concordia.inse6260.entities.Student;
import ca.concordia.inse6260.entities.User;
import ca.concordia.inse6260.entities.enums.Role;
import ca.concordia.inse6260.services.UserService;

@Component
public class DefaultUserService implements UserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCartService.class);

	@Resource
	private UserDAO userDao;

	@Resource
	private StudentDAO studentDao;
	
	@Resource
	private PasswordEncoder passwordEncoder;

	@Override
	public Iterable<Student> findAllStudents() {
		return studentDao.findAll();
	}

	@Override
	public Iterable<User> findAllProfessors() {
		Iterable<User> users = userDao.findAll();
		List<User> professors = new ArrayList<>();
		for (User user : users) {
			if (user.getRoles().contains(Role.ROLE_PROFESSOR)) {
				professors.add(user);
			}
		}
		return professors;
	}

	@Override
	public String changePasswordForUser(final String username, final String password, final String newPassword) {
		User user = userDao.findOne(username);
		if (user != null) {
			final String storedPassword = user.getPassword();
			final boolean passwordCheck = passwordEncoder.matches(password, storedPassword);
			if (passwordCheck) {
				String passwordCriteria = checkPasswordCriteria(newPassword);
				if(passwordCriteria.equals("OK")){
					user.setPassword(passwordEncoder.encode(newPassword));
					userDao.save(user);
				}
				else{
					LOGGER.info(passwordCriteria, username);
					return passwordCriteria;
				}
			} else {
				LOGGER.info("Incorrect user password: {}", username);
				return "Error: Incorrect user password!";
			}
		} else {
			LOGGER.debug("No user found with username: {}.", username);
			return "Error: No user found with username: " + username;
		}
		return "OK";
	}
	
	public String checkPasswordCriteria(String newPassword){
		String output = "";
		boolean tooLarge = true;
		boolean tooSmall = true;
		
		if(newPassword.length()>5){
			tooSmall = false;
		}
		if(newPassword.length()<17){
			tooLarge = false;
		}
		
		if(!tooSmall && !tooLarge){
			output = "OK";
		}
		else if(tooSmall){
			output = "Error: Password requires at least 6 characters";
		}
		else if(tooLarge){
			output = "Error: Password requires at most 16 characters";
		}
		else{
			output = "Error: Password does not meet criteria, must have length between 6-16 characters";
		}
		
		return output;
	}

	public UserDAO getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDAO userDao) {
		this.userDao = userDao;
	}

	public StudentDAO getStudentDao() {
		return studentDao;
	}

	public void setStudentDao(StudentDAO studentDao) {
		this.studentDao = studentDao;
	}
}
