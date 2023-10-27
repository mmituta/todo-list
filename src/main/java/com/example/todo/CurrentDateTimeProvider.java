package com.example.todo;

import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

/**
 * Is responsible for providing the current time.
 * Current time was hidden behind this abstraction to allow mocking the operation of getting the current time in the tests.
 */
@Component
public class CurrentDateTimeProvider {

    public OffsetDateTime now() {
        return OffsetDateTime.now();
    }
}
