package com.sanish.url;

import com.sanish.url.entities.User;
import com.sanish.url.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@SpringBootApplication
public class UrlShortenerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrlShortenerApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(UserRepository userRepository){
		return args -> {
			User user =  userRepository.findByUsername("radbrad")
					.orElseThrow(() -> new UsernameNotFoundException("Not found"));

			System.out.println(user);
		};
	}

}
