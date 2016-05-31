package ca.concordia.inse6260.config;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Resource
	private DataSource datasource;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/", "/home").permitAll().anyRequest().authenticated().and()//
				.formLogin().loginPage("/login").permitAll().and()//
				.logout().permitAll();
	}

	@Resource
	protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");

		auth.jdbcAuthentication().dataSource(datasource)
				.usersByUsernameQuery("select username,password,enabled from user where username=?")
				.authoritiesByUsernameQuery("select user_username,user_role_id from user_role where user_username=?");

		// JdbcUserDetailsManager userDetailsService = new JdbcUserDetailsManager();
		// userDetailsService.setDataSource(datasource);
		// PasswordEncoder encoder = new BCryptPasswordEncoder();
		//
		// auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
		// auth.jdbcAuthentication().dataSource(datasource);
		//
		// if(!userDetailsService.userExists("user")) {
		// List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		// authorities.add(new SimpleGrantedAuthority("USER"));
		// User userDetails = new User("user", encoder.encode("password"), authorities);
		//
		// userDetailsService.createUser(userDetails);
		// }
	}

}
