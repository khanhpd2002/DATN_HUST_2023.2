package vn.com.cardoctor.garage_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping(value = "/healthz", produces = "application/msn.api.v1+json")
    ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Success");
    }
}