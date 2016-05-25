package ca.concordia.inse6260.controllers;

import java.util.Calendar;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ca.concordia.inse6260.dao.CourseEntryDAO;
import ca.concordia.inse6260.entities.CourseEntry;
import ca.concordia.inse6260.entities.Season;

@RestController
public class CourseEntryController {

	@Resource
	private CourseEntryDAO dao;
	
	@RequestMapping(value="/courses", method=RequestMethod.GET)
	public @ResponseBody Iterable<CourseEntry> getCourses() {
		return dao.findAll();
	}
	
	@RequestMapping(value="/courses/{yearSeason}", method=RequestMethod.GET)
	public @ResponseBody Iterable<CourseEntry> getCoursesBySeason(@PathVariable("yearSeason") final String yearSeason) {
		if (yearSeason != null) {
			// FIXME - validate size
			System.out.println(">>> yearSeason : " + yearSeason);
			Season season = Season.valueOf(yearSeason.substring(0, yearSeason.length()-4));
			String year = yearSeason.substring(yearSeason.length()-4);
			Calendar cal = Calendar.getInstance();
			cal.set(Integer.parseInt(year), 0, 1, 0, 0, 0);
			return dao.findBySeason(season, cal);
		}
		return null;
	}
}
