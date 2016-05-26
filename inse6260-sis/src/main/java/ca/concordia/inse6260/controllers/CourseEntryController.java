package ca.concordia.inse6260.controllers;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ca.concordia.inse6260.entities.CourseEntry;
import ca.concordia.inse6260.services.CourseService;

@RestController
public class CourseEntryController {

	@Resource
	private CourseService courseService; 
	
	@RequestMapping(value="/courses", method=RequestMethod.GET)
	public @ResponseBody Iterable<CourseEntry> getCourses() {
		return courseService.findAll();
	}
	
	@RequestMapping(value="/courses/{yearSeason}", method=RequestMethod.GET)
	public @ResponseBody Iterable<CourseEntry> getCoursesBySeason(@PathVariable("yearSeason") final String yearSeason) {
		return courseService.findBySeason(yearSeason);
	}
}
