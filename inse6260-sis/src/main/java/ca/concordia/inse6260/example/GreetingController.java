package ca.concordia.inse6260.example;

import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
	
	@Resource
	private CustomerRepository repository;

	@RequestMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		Greeting found = null;
		if (!name.equals("World")) {
			long id = counter.incrementAndGet();
			Greeting greeting = new Greeting(id, String.format(template, name));
			repository.save(greeting);
			found = repository.findOne(id);
			System.out.println("!!! found = " + found);
		}
		Iterable<Greeting> its = repository.findAll();
		for (Greeting it: its) {
			System.out.println(">>>\t" + it);
		}
		return found;
	}
}
