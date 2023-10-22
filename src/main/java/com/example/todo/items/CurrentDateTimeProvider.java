package com.example.todo.items;

import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class CurrentDateTimeProvider {

    public OffsetDateTime now(){
        return OffsetDateTime.now();
    }
}
