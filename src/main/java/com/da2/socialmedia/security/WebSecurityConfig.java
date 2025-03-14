package com.da2.socialmedia.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig   {

    @Bean
    UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    @Order(1) // Higher precedence, evaluated first
    SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/admin/**") // Apply this configuration only to admin paths
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/admin/login").permitAll()
                                .anyRequest().hasRole("ADMIN")
                )
                .formLogin(login ->
                        login.loginPage("/admin/login")
                                .loginProcessingUrl("/admin/login")
                                .defaultSuccessUrl("/admin")
                                .permitAll()
                                .usernameParameter("email")
                )
                .logout(logout ->
                        logout.logoutUrl("/admin/logout")
                                .logoutSuccessUrl("/admin/login")
                                .permitAll()
                );

        return http.build();
    }

    @Bean
    @Order(2) // Lower precedence, evaluated second
    SecurityFilterChain userFilterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/new_post").authenticated()
                                .anyRequest().permitAll()
                )
                .formLogin(login ->
                        login.loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/")
                                .permitAll()
                                .usernameParameter("email")
                )
                .logout(logout ->
                        logout.logoutSuccessUrl("/")
                                .permitAll()
                );

        return http.build();
    }

//    @Bean
//    SecurityFilterChain configure(HttpSecurity http) throws Exception {
//
//        http.authenticationProvider(authenticationProvider());
//
////        http.authorizeHttpRequests(auth ->
////                        auth.requestMatchers("/admin/**").authenticated()
////                                .requestMatchers("admin_login").permitAll()
////                                .requestMatchers("admin/**").hasRole("ADMIN")
////                )
////                .formLogin(login ->
////                        login.usernameParameter("email")
////                                .defaultSuccessUrl("/admin/home")
////                                .permitAll()
////                                .loginPage("admin_login")
////                                .usernameParameter("email")
////                )
////                .logout(logout -> logout.logoutSuccessUrl("/admin_login").permitAll()
////                );
//
//        http.authorizeHttpRequests(auth ->
//                        auth.requestMatchers("/new_post").authenticated()
//                                .anyRequest().permitAll()
//                )
//                .formLogin(login ->
//                        login.usernameParameter("email")
//                                .defaultSuccessUrl("/")
//                                .permitAll()
//                                .loginPage("/login")
//                                .usernameParameter("email")
//                )
//                .logout(logout -> logout.logoutSuccessUrl("/").permitAll()
//                );
//
//
//        return http.build();
//    }
}
