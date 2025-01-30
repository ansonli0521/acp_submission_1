package com.example.acp_submission_1.controller;

import com.example.acp_submission_1.service.ValueManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
public class ApiController {

    @Autowired
    private ValueManagerService valueManagerService;

    // 1. uuid (HTTP GET)
    @GetMapping("/uuid")
    public String getUuid() {
        return "<!DOCTYPE html><html><head><title>Student ID</title></head><body><h1>12345678</h1></body></html>";
    }

    // 2. valuemanager (HTTP POST)
    @PostMapping("/valuemanager")
    public ResponseEntity<String> writeValue(@RequestParam String key, @RequestParam String value) {
        valueManagerService.writeValue(key, value);
        return ResponseEntity.ok("Value written successfully");
    }

    @PostMapping("/valuemanager/{key}/{value}")
    public ResponseEntity<String> writeValuePath(@PathVariable String key, @PathVariable String value) {
        valueManagerService.writeValue(key, value);
        return ResponseEntity.ok("Value written successfully");
    }

    // 3. valuemanager/key (HTTP DELETE)
    @DeleteMapping("/valuemanager/{key}")
    public ResponseEntity<String> deleteValue(@PathVariable String key) {
        if (valueManagerService.deleteValue(key)) {
            return ResponseEntity.ok("Value deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Key not found");
        }
    }

    // 4. valuemanager/key (HTTP GET)
    @GetMapping("/valuemanager/{key}")
    public ResponseEntity<String> getValue(@PathVariable String key) {
        String value = valueManagerService.getValue(key);
        if (value != null) {
            return ResponseEntity.ok().body(value);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/valuemanager")
    public ResponseEntity<Map<String, String>> getAllValues() {
        return ResponseEntity.ok().body(valueManagerService.getAllValues());
    }

    // 5. callservice (HTTP POST)
    @PostMapping("/callservice")
    public ResponseEntity<String> callService(@RequestBody Map<String, String> request) {
        String externalBaseUrl = request.get("externalBaseUrl");
        String parameters = request.get("parameters");

        if (externalBaseUrl == null || parameters == null) {
            return ResponseEntity.badRequest().body("Invalid request body");
        }

        String url = externalBaseUrl + parameters;
        RestTemplate restTemplate = new RestTemplate();
        try {
            String response = restTemplate.getForObject(url, String.class);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to call external service");
        }
    }
}
