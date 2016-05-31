package ca.concordia.inse6260.services;

import java.util.List;

import ca.concordia.inse6260.entities.CourseEntry;
import ca.concordia.inse6260.entities.Season;

public interface CourseService {

	Iterable<CourseEntry> findAll();

	/**
	 * Find course entry by string with year and season.
	 * 
	 * @param yearSeason
	 *            The search string in the format "{season}{year}" where {season} is any value in {@link Season} and {year} is a 4
	 *            digits number (e.g. "SUMMER2016")
	 * @return The list of matching CourseEntry.
	 * @throws IllegalArgumentException
	 *             if String is in wrong format.
	 */
	List<CourseEntry> findBySeason(final String yearSeason);
}
