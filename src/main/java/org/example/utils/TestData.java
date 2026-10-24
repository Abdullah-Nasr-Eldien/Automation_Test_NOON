package org.example.utils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class TestData {

    private static final JsonNode data;

    static {
        try (InputStream input = TestData.class
                .getClassLoader()
                .getResourceAsStream("testData.json")) {

            if (input == null) {
                throw new RuntimeException("testData.json not found");
            }

            data = new ObjectMapper().readTree(input);

        } catch (IOException e) {
            throw new RuntimeException("Failed to read testData.json", e);
        }
    }

    public static String get(String key) {
        JsonNode value = data;
        for (String part : key.split("\\.")) {
            value = value == null ? null : value.get(part);
        }

        if (value == null || value.isNull() || !value.isValueNode()) {
            throw new IllegalArgumentException(
                    "Missing test data key: " + key
            );
        }

        return value.asText();
    }
}
