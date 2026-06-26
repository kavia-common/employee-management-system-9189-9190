package com.kavia.employees.config;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

  @Value("${app.cors.allowed-origins:http://localhost:3000}")
  private String allowedOrigins;

  @Value("${app.cors.allowed-methods:GET,POST,PUT,DELETE,PATCH,OPTIONS}")
  private String allowedMethods;

  @Value("${app.cors.allowed-headers:Content-Type,Authorization,X-Requested-With}")
  private String allowedHeaders;

  @Value("${app.cors.max-age:3600}")
  private long maxAge;

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();

    List<String> origins = Arrays.stream(allowedOrigins.split(",")).map(String::trim).filter(s -> !s.isBlank()).toList();
    config.setAllowedOrigins(origins);

    config.setAllowedMethods(Arrays.stream(allowedMethods.split(",")).map(String::trim).filter(s -> !s.isBlank()).toList());
    config.setAllowedHeaders(Arrays.stream(allowedHeaders.split(",")).map(String::trim).filter(s -> !s.isBlank()).toList());

    config.setAllowCredentials(true);
    config.setMaxAge(maxAge);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }
}
