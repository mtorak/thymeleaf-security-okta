package com.okta.developer.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
public class QuizController {

  private static Logger logger = LoggerFactory.getLogger(QuizController.class);

  @GetMapping("/quiz")
  @PreAuthorize("hasAuthority('SCOPE_quiz')")
  public Mono<Rendering> showQuiz() {
    return Mono.just(Rendering.view("quiz").modelAttribute("quiz", new QuizSubmission()).build());
  }

  @PostMapping(path = "/quiz", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
  @PreAuthorize("hasAuthority('SCOPE_quiz')")
  public Mono<Rendering> saveQuiz(QuizSubmission quizSubmission) {
    return Mono.just(Rendering.view("result").modelAttribute("quiz", quizSubmission).build());
  }

}
