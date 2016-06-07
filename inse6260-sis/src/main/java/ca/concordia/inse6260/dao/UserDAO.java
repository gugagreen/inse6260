package ca.concordia.inse6260.dao;

import org.springframework.data.repository.CrudRepository;

import ca.concordia.inse6260.entities.User;

public interface UserDAO extends CrudRepository<User, String> {

}
