package ca.concordia.inse6260.dao;

import org.springframework.data.repository.CrudRepository;

import ca.concordia.inse6260.entities.CourseEntry;

public interface CourseEntryDAO extends CrudRepository<CourseEntry, Long> {
}
