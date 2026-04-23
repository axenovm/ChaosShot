package org.example.orderservice.entity;

import java.util.UUID;

public record Bar(UUID id,
                  String barName,
                  String address) {}
