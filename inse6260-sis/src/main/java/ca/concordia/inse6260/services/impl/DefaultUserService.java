package ca.concordia.inse6260.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ca.concordia.inse6260.dao.StudentDAO;
import ca.concordia.inse6260.dao.UserDAO;
import ca.concordia.inse6260.entities.AcademicRecordEntry;
import ca.concordia.inse6260.entities.AcademicRecordStatus;
import ca.concordia.inse6260.entities.CourseEntry;
import ca.concordia.inse6260.entities.Student;
import ca.concordia.inse6260.entities.User;
import ca.concordia.inse6260.services.UserService;

@Component
public class DefaultUserService implements UserService {
private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCartService.class);
	@Resource
	private StudentDAO studentDao;
	
	@Resource 
	private UserDAO userDao;

	@Override
	public Iterable<Student> findAllStudents() {
		return studentDao.findAll();
	}

	public StudentDAO getStudentDao() {
		return studentDao;
	}

	public void setStudentDao(StudentDAO studentDao) {
		this.studentDao = studentDao;
	}
	@Override
	public void changePasswordForUser(final String username, final String password, final String newPassword) {
		User user = userDao.findOne(username);
		if (user != null) {
			final String storedPassword = user.getPassword();
			final boolean passwordCheck = storedPassword.equals(password);
			if (passwordCheck) {
				
				user.setPassword(newPassword);;
				// TODO - treat other status
				userDao.save(user);
			} 
			else {
				LOGGER.info("Incorrect user password: {}", username);
				// FIXME - throw exception
			}
		} else {
			LOGGER.debug("No user found with username: {}.", username);
			// FIXME - throw exception
		}
	}
}
