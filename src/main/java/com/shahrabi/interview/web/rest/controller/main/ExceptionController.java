package com.shahrabi.interview.web.rest.controller.main;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/exception")
public class ExceptionController {

    @GetMapping
    public ResponseEntity<Void> ping() {
        throw new RuntimeException("hello");
    }
}
