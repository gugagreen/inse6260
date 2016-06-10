package ca.concordia.inse6260.controllers;

import ca.concordia.inse6260.services.UserService;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountRegistrationController {
	
	@Resource
	private UserService userService;
	
    // change user password
    @RequestMapping(value = "/changePassword/user/{username}/password/{password}/newPassword/{newPassword}", method = RequestMethod.POST)
    public @ResponseBody void changePasswordForUser(@PathVariable("username") final String username,@PathVariable("password") String password,@PathVariable("newPassword") String newPassword){
    	userService.changePasswordForUser(username, password, newPassword);
    }
	
	
}
