package org.example.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.orderservice.entity.OrderPromotion;
import org.example.orderservice.entity.PromotionEvent;
import org.example.orderservice.repository.OrderPromotionRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class PromotionEventConsumer {
    private final OrderPromotionRepository orderPromotionRepository;
    private final ObjectMapper objectMapper;
    private final Logger log = Logger.getLogger(PromotionEventConsumer.class.getName());

    @KafkaListener(
            topics = "${kafka.topics.promotions:promotions-events}",
            groupId = "${spring.kafka.consumer.group-id:order-service-group}"
    )
    public void consumePromotionEvent(String message) {
        try {
            PromotionEvent event = objectMapper.readValue(message, PromotionEvent.class);
            OrderPromotion orderPromotion = new OrderPromotion(
                    event.promotionId(),
                    event.eventId(),
                    event.name(),
                    event.discountPercent(),
                    event.startsAt(),
                    event.endsAt(),
                    event.active()
            );
            orderPromotionRepository.upsert(orderPromotion);
            log.info("Promotion event consumed: " + event.eventId());
        } catch (JsonProcessingException e) {
            log.log(Level.WARNING, "Failed to parse promotion event message", e);
        } catch (RuntimeException e) {
            log.log(Level.WARNING, "Failed to process promotion event message", e);
        }
    }

}
