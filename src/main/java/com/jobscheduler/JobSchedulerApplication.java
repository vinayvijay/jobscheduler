package com.jobscheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.jobscheduler.filter.JwtFilter;

@SpringBootApplication
@EnableScheduling
public class JobSchedulerApplication   extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(JobSchedulerApplication.class, args);
	}
	
    @Bean
    public FilterRegistrationBean jwtFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new JwtFilter());
        registrationBean.addUrlPatterns("/jobscheduler/workorder");
        registrationBean.addUrlPatterns("/jobscheduler/myschedule");

        return registrationBean;
    }
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    	
        return application.sources(JobSchedulerApplication.class);
    }
    
    @Bean
    static PropertySourcesPlaceholderConfigurer propertyPlaceHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
    
}
