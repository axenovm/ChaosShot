package org.example.promotionservice.entity;

import java.time.LocalDateTime;
import java.util.UUID;

public record Outbox(UUID id,
                     UUID promotionId,
                     String eventType,
                     LocalDateTime createdAt,
                     Boolean processed) {}
