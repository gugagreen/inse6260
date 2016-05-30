package ca.concordia.inse6260.controllers;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ca.concordia.inse6260.entities.AcademicRecordEntry;
import ca.concordia.inse6260.services.CartService;

@RestController
public class CartController {
	
	@Resource
	private CartService cartService;

	@RequestMapping(value="/cart/student/{username}", method=RequestMethod.GET)
	public @ResponseBody Iterable<AcademicRecordEntry> getCartByStudent(@PathVariable("username") final String username) {
		return cartService.findCartByStudent(username);
	}
}
