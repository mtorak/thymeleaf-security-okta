package com.okta.developer.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

  private static Logger logger = LoggerFactory.getLogger(HomeController.class);

  @GetMapping("/")
  public Mono<Rendering> home(Authentication authentication) {
    List<String> authorities = authentication.getAuthorities()
        .stream()
        .map(scope -> scope.toString())
        .collect(Collectors.toList());
    return Mono.just(Rendering.view("home").modelAttribute("authorities", authorities).build());
  }

  @GetMapping("/profile")
  @PreAuthorize("hasAuthority('SCOPE_profile')")
  public Mono<Rendering> userDetails(OAuth2AuthenticationToken authentication) {
    return Mono.just(Rendering.view("userProfile")
        .modelAttribute("details", authentication.getPrincipal().getAttributes())
        .build());
  }

}
