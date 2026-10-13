
package com.inventory.skillgapanalysisbackend.config;

import com.inventory.skillgapanalysisbackend.Security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class SecurityConfig {

 private final JwtAuthenticationFilter jwtFilter;

 public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
  this.jwtFilter = jwtFilter;
 }

 @Bean
 public PasswordEncoder passwordEncoder() {
  return new BCryptPasswordEncoder();
 }

 @Bean
 public SecurityFilterChain securityFilterChain(
         HttpSecurity http) throws Exception {

  http
          .csrf(csrf -> csrf.disable())
          .cors(cors -> cors.configurationSource(request -> {
           CorsConfiguration config = new CorsConfiguration();
           config.setAllowedOrigins(
                   List.of("http://localhost:5173"));
           config.setAllowedMethods(
                   List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
           config.setAllowedHeaders(List.of("*"));
           return config;
          }))
          .sessionManagement(session ->
                  session.sessionCreationPolicy(
                          SessionCreationPolicy.STATELESS))

          .authorizeHttpRequests(auth -> auth
                  .anyRequest().permitAll())

          .addFilterBefore(
                  jwtFilter,
                  UsernamePasswordAuthenticationFilter.class);

  return http.build();
 }
}
