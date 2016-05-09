package ca.concordia.inse6260.controllers;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ca.concordia.inse6260.dao.CourseEntryDAO;
import ca.concordia.inse6260.entities.CourseEntry;

@RestController
public class CourseEntryController {

	@Resource
	private CourseEntryDAO dao;
	
	@RequestMapping(value="/courses", method=RequestMethod.GET)
	public @ResponseBody Iterable<CourseEntry> getCourses() {
		System.out.println("\n\n\n" + dao.findAll() + "<<<\n\n"); // FIXME - delete
		return dao.findAll();
	}
}
