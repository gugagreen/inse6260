package ca.concordia.inse6260.services;

import ca.concordia.inse6260.entities.Student;
import ca.concordia.inse6260.entities.User;

public interface UserService {
	/**
	 * Find all students in system.
	 * @return
	 */
	Iterable<Student> findAllStudents();
	
	/**
	 * Find all professors in system.
	 * @return
	 */
	Iterable<User> findAllProfessors();
}
