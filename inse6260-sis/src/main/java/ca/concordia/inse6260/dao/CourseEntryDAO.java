package ca.concordia.inse6260.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.concordia.inse6260.entities.CourseEntry;
import ca.concordia.inse6260.entities.Season;

public interface CourseEntryDAO extends CrudRepository<CourseEntry, Long> {

	List<CourseEntry> findByDatesSeason(Season season);
}
