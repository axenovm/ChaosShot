package org.example.promotionservice.entity;

import java.time.LocalDateTime;
import java.util.UUID;

public record Promotion(UUID id,
                        String name,
                        Integer discountPercent,
                        LocalDateTime startsAt,
                        LocalDateTime endsAt,
                        Boolean isActive) {}
