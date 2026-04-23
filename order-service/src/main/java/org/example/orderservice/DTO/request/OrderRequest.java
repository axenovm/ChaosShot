package org.example.orderservice.DTO.request;

import java.util.List;
import java.util.UUID;

public record OrderRequest(UUID userId,
                           UUID barId,
                           String number,
                           List<OrderItemRequest> items,
                           Integer version) {
}
