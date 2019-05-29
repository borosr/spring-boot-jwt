package hu.flowacademy.authserver.rest;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secure")
public class SecureResource {

  @GetMapping("/")
  public List<String> getData() {
    return List.of("secure 1", "secure 2", "secure 3");
  }

}
