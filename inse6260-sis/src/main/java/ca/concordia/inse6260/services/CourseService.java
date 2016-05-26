package ca.concordia.inse6260.services;

import java.util.List;

import ca.concordia.inse6260.entities.CourseEntry;

public interface CourseService {

	Iterable<CourseEntry> findAll();
	
	List<CourseEntry> findBySeason(String yearSeason);
}
