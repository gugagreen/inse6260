package ca.concordia.inse6260.example;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Greeting, Long> {

	List<Greeting> findByContent(String content);
}
