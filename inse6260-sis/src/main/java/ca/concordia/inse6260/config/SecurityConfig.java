package ca.concordia.inse6260.config;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Resource
	private DataSource datasource;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			
			.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
			.logout()
				.permitAll()
				.and()
			.authorizeRequests()
				.antMatchers("/css/**").permitAll()
				.anyRequest().authenticated();
	}

	@Resource
	protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");

		auth.jdbcAuthentication().dataSource(datasource)
				.passwordEncoder(passwordEncoder())
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
	
	@Bean
	public PasswordEncoder passwordEncoder(){
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}

}
