package ca.concordia.inse6260.services.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ca.concordia.inse6260.dao.CourseEntryDAO;
import ca.concordia.inse6260.entities.CourseEntry;
import ca.concordia.inse6260.entities.Season;
import ca.concordia.inse6260.services.CourseService;

@Component
public class DefaultCourseService implements CourseService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCourseService.class);

	@Resource
	private CourseEntryDAO dao;

	public Iterable<CourseEntry> findAll() {
		return dao.findAll();
	}

	@Override
	public List<CourseEntry> findBySeason(final String yearSeason) {
		LOGGER.debug("Find course by year season: {}", yearSeason);
		List<CourseEntry> courses = new ArrayList<CourseEntry>();
		validateYearSeason(yearSeason);
		Season season = Season.valueOf(yearSeason.substring(0, yearSeason.length() - 4));
		String year = yearSeason.substring(yearSeason.length() - 4);
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(year), 0, 1, 0, 0, 0);
		courses = dao.findBySeason(season, cal);
		return courses;
	}

	private void validateYearSeason(final String yearSeason) {
		if ((yearSeason != null) && (yearSeason.length() > 4)) {
			// throws IllegalArgumentException if enum does not exist
			Season.valueOf(yearSeason.substring(0, yearSeason.length() - 4));
		} else {
			throw new IllegalArgumentException("Invalid yearSeason: " + yearSeason);
		}
	}

	public CourseEntryDAO getDao() {
		return dao;
	}

	public void setDao(CourseEntryDAO dao) {
		this.dao = dao;
	}

}
