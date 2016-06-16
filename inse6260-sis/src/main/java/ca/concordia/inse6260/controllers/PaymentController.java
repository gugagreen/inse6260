package ca.concordia.inse6260.controllers;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ca.concordia.inse6260.entities.dto.AccountBalance;
import ca.concordia.inse6260.services.PaymentService;

@RestController
public class PaymentController {

	@Resource
	private PaymentService service;
	
	@RequestMapping(value="/payment/student/{username}/value/{value}", method=RequestMethod.POST)
	public void addPayment(@PathVariable("username") final String username, @PathVariable("username") final BigDecimal value) {
		service.addPayment(username, value);
	}
	
	@RequestMapping(value="/payment/student/{username}", method=RequestMethod.GET)
	public AccountBalance getBalance(@PathVariable("username") final String username) {
		return service.getBalance(username);
	}
}
