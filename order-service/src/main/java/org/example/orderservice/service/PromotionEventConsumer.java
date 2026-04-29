package org.example.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.orderservice.entity.PromotionEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class PromotionEventConsumer {
    private final OrderPromotionService orderPromotionService;
    private final ObjectMapper objectMapper;
    private final Logger log = Logger.getLogger(PromotionEventConsumer.class.getName());

    @KafkaListener(
            topics = "${kafka.topics.promotions:promotions-events}",
            groupId = "${spring.kafka.consumer.group-id:order-service-group}"
    )
    public void consumePromotionEvent(String message) {
        try {
            PromotionEvent event = objectMapper.readValue(message, PromotionEvent.class);
            log.info("Promotion event consumed: " + event.eventId());
            String type = event.eventType();

            switch (type) {
                case "PROMOTION_CREATED":
                    orderPromotionService.addPromotion(event);
                    log.info("Promotion upsert handled: " + event.eventId());
                    break;
                case "PROMOTION_DEACTIVATED":
                    orderPromotionService.deactivatePromotion(event.promotionId());
                    log.info("Promotion deactivate handled: " + event.eventId());
                    break;
                default:
                        log.warning("Unknown event type: " + type);
            }


        } catch (JsonProcessingException e) {
            log.log(Level.WARNING, "Failed to parse promotion event message", e);
        } catch (RuntimeException e) {
            log.log(Level.WARNING, "Failed to process promotion event message", e);
        }
    }

}
