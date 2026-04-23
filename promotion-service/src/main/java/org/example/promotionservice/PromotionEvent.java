package org.example.promotionservice;

import java.time.LocalDateTime;
import java.util.UUID;

public record PromotionEvent(
        UUID eventId,
        String eventType,
        LocalDateTime occurredAt,
        UUID promotionId,
        String name,
        Integer discountPercent,
        LocalDateTime startsAt,
        LocalDateTime endsAt,
        Boolean active) {}
