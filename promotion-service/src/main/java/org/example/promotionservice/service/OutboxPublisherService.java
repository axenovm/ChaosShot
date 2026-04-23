package org.example.promotionservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.promotionservice.PromotionEvent;
import org.example.promotionservice.entity.Outbox;
import org.example.promotionservice.entity.Promotion;
import org.example.promotionservice.repository.OutboxRepository;
import org.example.promotionservice.repository.PromotionRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class OutboxPublisherService {

    private final OutboxRepository outboxRepository;
    private final PromotionRepository promotionRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final Logger log = Logger.getLogger(OutboxPublisherService.class.getName());

    public void publishPending(Integer batchSize, String topicName) {
        List<Outbox> pendingEvents = outboxRepository.findPending(batchSize);
        for (Outbox outbox : pendingEvents) {
            Optional<Promotion> promotion = promotionRepository.findById(outbox.promotionId());
            if (promotion.isEmpty()) {
                log.warning("Promotion not found for outbox id " + outbox.id() + ". Marking as processed.");
                outboxRepository.markProcessed(outbox.id());
                continue;
            }

            PromotionEvent promotionEvent = new PromotionEvent(
                    UUID.randomUUID(),
                    outbox.eventType(),
                    LocalDateTime.now(),
                    promotion.get().id(),
                    promotion.get().name(),
                    promotion.get().discountPercent(),
                    promotion.get().startsAt(),
                    promotion.get().endsAt(),
                    promotion.get().isActive());

            try {
                String message = objectMapper.writeValueAsString(promotionEvent);
                kafkaTemplate.send(topicName, message);
                outboxRepository.markProcessed(outbox.id());
                log.info("Outbox event sent: " + outbox.id());
            } catch (JsonProcessingException e) {
                log.log(Level.WARNING, "Outbox serialization failed for id " + outbox.id(), e);
            } catch (RuntimeException e) {
                log.log(Level.WARNING, "Outbox send failed for id " + outbox.id(), e);
            }
        }
    }
}
