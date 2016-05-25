package ca.concordia.inse6260.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.concordia.inse6260.entities.CourseDates;

public interface CourseDatesDAO extends CrudRepository<CourseDates, Long> {

	List<String> findSeasons();
}
