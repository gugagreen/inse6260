package ca.concordia.inse6260.services.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import ca.concordia.inse6260.dao.StudentDAO;
import ca.concordia.inse6260.entities.Student;
import ca.concordia.inse6260.services.UserService;

@Component
public class DefaultUserService implements UserService {
	
	@Resource
	private StudentDAO studentDao;

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

}
