package ca.concordia.inse6260.dao;

import org.springframework.data.repository.CrudRepository;

import ca.concordia.inse6260.entities.Student;

public interface StudentDAO extends CrudRepository<Student, String> {

}
