package com.example.todo.items;

import com.example.todo.items.repository.Status;


public record ItemUpdate(String description, Status status) {
}
