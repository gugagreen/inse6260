package ca.concordia.inse6260.controllers;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ca.concordia.inse6260.dao.CourseDatesDAO;

@RestController
public class CourseDatesController {

	@Resource
	private CourseDatesDAO dao;
	
	@RequestMapping(value="/courseDates", method=RequestMethod.GET)
	public @ResponseBody Iterable<String> getCourses() {
		return dao.findSeasons();
	}
}
