package ca.concordia.inse6260.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/home").setViewName("home");
		registry.addViewController("/").setViewName("home");
		registry.addViewController("/cart").setViewName("cart");
		registry.addViewController("/transcript").setViewName("transcript");
		registry.addViewController("/payment").setViewName("payment");
		registry.addViewController("/schedule").setViewName("schedule");
		registry.addViewController("/grades").setViewName("grades");
		registry.addViewController("/login").setViewName("login");
	}

}
