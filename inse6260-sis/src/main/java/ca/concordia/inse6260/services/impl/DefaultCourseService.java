package ca.concordia.inse6260.services.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import ca.concordia.inse6260.dao.CourseEntryDAO;
import ca.concordia.inse6260.entities.CourseEntry;
import ca.concordia.inse6260.entities.Season;
import ca.concordia.inse6260.services.CourseService;

@Component
public class DefaultCourseService implements CourseService {
	
	@Resource
	private CourseEntryDAO dao;
	
	public Iterable<CourseEntry> findAll() {
		return dao.findAll();
	}

	@Override
	public List<CourseEntry> findBySeason(String yearSeason) {
		List<CourseEntry> courses = new ArrayList<CourseEntry>();
		if (yearSeason != null) {
			// FIXME - validate size
			System.out.println(">>> yearSeason : " + yearSeason);
			Season season = Season.valueOf(yearSeason.substring(0, yearSeason.length()-4));
			String year = yearSeason.substring(yearSeason.length()-4);
			Calendar cal = Calendar.getInstance();
			cal.set(Integer.parseInt(year), 0, 1, 0, 0, 0);
			courses = dao.findBySeason(season, cal);
		}
		return courses;
	}

}
