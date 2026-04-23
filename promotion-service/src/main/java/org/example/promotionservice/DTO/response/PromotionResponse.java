package org.example.promotionservice.DTO.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record PromotionResponse(UUID id,
                                String name,
                                Integer discountPercent,
                                LocalDateTime startsAt,
                                LocalDateTime endsAt,
                                boolean isActive) {
}
