package hu.flowacademy.authserver.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicResource {

  @GetMapping("/")
  public String sayHello() {
    return "Welcome on my JWT example page!";
  }

}
