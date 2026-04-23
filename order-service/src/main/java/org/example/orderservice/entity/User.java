package org.example.orderservice.entity;

import jdk.jfr.DataAmount;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record User(UUID id,
                   String email,
                   LocalDateTime createdAt,
                   String username,
                   String password) {}
