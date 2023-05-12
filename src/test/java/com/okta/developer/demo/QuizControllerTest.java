package com.okta.developer.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOidcLogin;

@WebFluxTest
public class QuizControllerTest {

  @Autowired
  private WebTestClient client;

  @Test
  void testPostQuiz_noCSRFToken() throws Exception {
    QuizSubmission quizSubmission = new QuizSubmission();
    this.client.mutateWith(mockOidcLogin())
        .post().uri("/quiz")
        .exchange()
        .expectStatus().isForbidden()
        .expectBody().returnResult()
        .toString().contains("An expected CSRF token cannot be found");
  }

  @Test
  void testPostQuiz() throws Exception {
    this.client.mutateWith(csrf()).mutateWith(mockOidcLogin())
        .post().uri("/quiz")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .exchange().expectStatus().isOk();
  }

  @Test
  void testGetQuiz_noAuth() throws Exception {
    this.client.get().uri("/quiz").exchange().expectStatus().is3xxRedirection();
  }

  @Test
  void testGetQuiz() throws Exception {
    this.client.mutateWith(mockOidcLogin())
        .get().uri("/quiz").exchange().expectStatus().isOk();
  }

}