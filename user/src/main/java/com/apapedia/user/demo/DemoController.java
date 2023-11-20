package com.apapedia.user.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo-controller")
public class DemoController {

<<<<<<< HEAD
<<<<<<< HEAD
  @GetMapping
  public ResponseEntity<String> sayHello() {
    return ResponseEntity.ok("Hello from secured endpoint");
  }
=======
=======
>>>>>>> 2160e5ce41fdfc53c6121668711502aa8061d87d
    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello from secured endpoint");
    }
<<<<<<< HEAD
>>>>>>> 3df004dc477b2fcdf40967613090d373b77d4980
=======
>>>>>>> 2160e5ce41fdfc53c6121668711502aa8061d87d

}