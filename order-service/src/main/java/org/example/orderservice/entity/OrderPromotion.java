package org.example.orderservice.entity;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderPromotion(
        UUID promotionId,
        UUID eventId,
        String name,
        Integer discountPercent,
        LocalDateTime startsAt,
        LocalDateTime endsAt,
        Boolean active) {}
