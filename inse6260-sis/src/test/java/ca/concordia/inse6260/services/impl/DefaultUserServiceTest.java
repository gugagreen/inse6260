package ca.concordia.inse6260.services.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ca.concordia.inse6260.dao.StudentDAO;
import ca.concordia.inse6260.dao.UserDAO;
import ca.concordia.inse6260.entities.User;
import ca.concordia.inse6260.entities.enums.Role;

@RunWith(MockitoJUnitRunner.class)
public class DefaultUserServiceTest {

	private DefaultUserService service;

	@Mock
	private UserDAO userDao;

	@Mock
	private StudentDAO studentDao;

	@Before
	public void setup() {
		service = new DefaultUserService();
		service.setUserDao(userDao);
		service.setStudentDao(studentDao);
	}

	@Test
	public void shouldReturnOnlyProfessors() {
		User[] users = { buildUser("s", "1", Role.ROLE_STUDENT), buildUser("p1", "1", Role.ROLE_PROFESSOR),
				buildUser("a", "1", Role.ROLE_ADMIN), buildUser("p2", "1", Role.ROLE_PROFESSOR) };
		Mockito.when(userDao.findAll()).thenReturn(Arrays.asList(users));
		Iterable<User> professors = service.findAllProfessors();
		Assert.assertNotNull(professors);
		Assert.assertEquals(2, ((Collection<?>)professors).size());
		for (User prof : professors) {
			Assert.assertTrue(prof.getRoles().contains(Role.ROLE_PROFESSOR));
		}
	}
	
	@Test
	public void shouldNotChangePasswordWithWrongOldPassword() {
		String username = "test";
		String oldPassword = "old";
		String badOldPassword = "bad";
		String newPassword = "new";
		User user = buildUser(username, oldPassword, Role.ROLE_STUDENT);
		
		Mockito.when(userDao.findOne(username)).thenReturn(user);
		
		String response = service.changePasswordForUser(username, badOldPassword, newPassword);
		Assert.assertNotNull(response);
		System.out.println(response);
		Assert.assertTrue(response.contains("Error"));
	}

	private User buildUser(String username, String password, Role role) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		HashSet<Role> roles = new HashSet<>();
		roles.add(role);
		user.setRoles(roles);
		return user;
	}
}
