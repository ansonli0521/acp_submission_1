package com.example.acp_submission_1.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ValueManagerService {

    private final Map<String, String> keyValueStore = new HashMap<>();

    public void writeValue(String key, String value) {
        keyValueStore.put(key, value);
    }

    public String getValue(String key) {
        return keyValueStore.get(key);
    }

    public boolean deleteValue(String key) {
        return keyValueStore.remove(key) != null;
    }

    public Map<String, String> getAllValues() {
        return new HashMap<>(keyValueStore);
    }
}