package com.okta.developer.demo;

import java.net.URI;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

  @Bean
  public ServerLogoutSuccessHandler logoutSuccessHandler() {
    RedirectServerLogoutSuccessHandler handler = new RedirectServerLogoutSuccessHandler();
    handler.setLogoutSuccessUrl(URI.create("/"));
    return handler;
  }

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    http
        .authorizeExchange().pathMatchers("/").permitAll().and().anonymous()
        .and().authorizeExchange().anyExchange().authenticated()
        .and().oauth2Client()
        .and().oauth2Login()
        .and().logout().logoutSuccessHandler(logoutSuccessHandler())
        .and().exceptionHandling().accessDeniedHandler((exchange, denied) -> {
          ServerHttpResponse response = exchange.getResponse();
          response.setStatusCode(HttpStatus.PERMANENT_REDIRECT);
          response.getHeaders().setLocation(URI.create("/"));
          return response.setComplete();
        });

    return http.build();
  }

}