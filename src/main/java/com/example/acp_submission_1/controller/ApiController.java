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
        return "<!DOCTYPE html><html><head><title>Student ID</title></head><body><h1>s1943226</h1></body></html>";
    }

    // 2. valuemanager (HTTP POST)
    @PostMapping("/valuemanager")
    public ResponseEntity<Void> writeValue(@RequestParam String key, @RequestParam String value) {
        if (key == null || key.isEmpty() || value == null || value.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        valueManagerService.writeValue(key, value);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/valuemanager/{key}/{value}")
    public ResponseEntity<Void> writeValuePath(@PathVariable String key, @PathVariable String value) {
        if (key == null || key.isEmpty() || value == null || value.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        valueManagerService.writeValue(key, value);
        return ResponseEntity.ok().build();
    }

    // 3. valuemanager/key (HTTP DELETE)
    @DeleteMapping("/valuemanager/{key}")
    public ResponseEntity<Void> deleteValue(@PathVariable String key) {
        if (key == null || key.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (valueManagerService.deleteValue(key)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 4. valuemanager/key (HTTP GET)
    @GetMapping("/valuemanager/{key}")
    public ResponseEntity<String> getValue(@PathVariable String key) {
        if (key == null || key.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        String value = valueManagerService.getValue(key);
        if (value != null) {
            return ResponseEntity.ok().body(value);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/valuemanager/")
    public ResponseEntity<Map<String, String>> getAllValues() {
        Map<String, String> allValues = valueManagerService.getAllValues();
        if (allValues.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().body(allValues);
    }

    // 5. callservice (HTTP POST)
    @PostMapping("/callservice")
    public ResponseEntity<String> callService(@RequestBody Map<String, String> request) {
        String externalBaseUrl = request.get("externalBaseUrl");
        String parameters = request.get("parameters");

        if (externalBaseUrl == null || externalBaseUrl.isEmpty() || parameters == null || parameters.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        String url;
        if (externalBaseUrl.endsWith("/") && parameters.startsWith("/")) {
            url = externalBaseUrl + parameters.substring(1);
        } else if (externalBaseUrl.endsWith("/") || parameters.startsWith("/")) {
            url = externalBaseUrl + parameters;
        } else {
            url = externalBaseUrl + "/" + parameters;
        }

        RestTemplate restTemplate = new RestTemplate();
        try {
            String response = restTemplate.getForObject(url, String.class);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}