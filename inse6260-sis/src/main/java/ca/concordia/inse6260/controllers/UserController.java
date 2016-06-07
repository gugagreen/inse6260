package ca.concordia.inse6260.controllers;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ca.concordia.inse6260.entities.Student;
import ca.concordia.inse6260.entities.User;
import ca.concordia.inse6260.services.UserService;

@RestController
public class UserController {
	
	@Resource
	private UserService service;
	
	@RequestMapping(value="/user/student/", method=RequestMethod.GET)
	public @ResponseBody Iterable<Student> getStudents() {
		return service.findAllStudents();
	}
	
	@RequestMapping(value="/user/professor/", method=RequestMethod.GET)
	public @ResponseBody Iterable<User> getProfessors() {
		return service.findAllProfessors();
	}
}