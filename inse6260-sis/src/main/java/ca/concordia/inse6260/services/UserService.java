package ca.concordia.inse6260.services;

import ca.concordia.inse6260.entities.Student;

public interface UserService {
	/**
	 * Find all students in system.
	 * @return
	 */
	Iterable<Student> findAllStudents();

	void changePasswordForUser(String username, String password, String newPassword);
	
	
}
