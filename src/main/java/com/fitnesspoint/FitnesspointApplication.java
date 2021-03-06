package com.fitnesspoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fitnesspoint.services.UserService;

@SpringBootApplication
public class FitnesspointApplication {
	
	@Autowired
	private UserService usServ;

	public static void main(String[] args) {
		SpringApplication.run(FitnesspointApplication.class, args);
	}

	   @Autowired
	    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	        auth
	                .userDetailsService(usServ)
	                .passwordEncoder(new BCryptPasswordEncoder());

	    }	
	
}
