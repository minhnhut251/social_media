package com.da2.socialmedia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@SpringBootApplication
public class SocialmediaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialmediaApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}

@Configuration
@EnableWebSecurity
class SecurityConfig {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/login", "/register").permitAll()
						.anyRequest().authenticated()
				)
				.formLogin();
		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService(DataSource dataSource, BCryptPasswordEncoder passwordEncoder) {
		JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
		if (!userDetailsManager.userExists("user")) {
			UserDetails user = User.builder()
					.username("user")
					.password(passwordEncoder.encode("password"))
					.roles("USER")
					.build();
			userDetailsManager.createUser(user);
		}
		return userDetailsManager;
	}
}
