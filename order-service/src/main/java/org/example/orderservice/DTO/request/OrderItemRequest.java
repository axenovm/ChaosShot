package org.example.orderservice.DTO.request;

import java.util.UUID;

public record OrderItemRequest(UUID menuItemId, Integer quantity) {
}
