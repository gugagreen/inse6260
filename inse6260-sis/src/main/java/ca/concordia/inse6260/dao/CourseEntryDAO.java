package ca.concordia.inse6260.dao;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.concordia.inse6260.entities.CourseEntry;
import ca.concordia.inse6260.entities.enums.Season;

public interface CourseEntryDAO extends CrudRepository<CourseEntry, Long> {

	List<CourseEntry> findBySeason(Season season, Calendar yearDate);
}
