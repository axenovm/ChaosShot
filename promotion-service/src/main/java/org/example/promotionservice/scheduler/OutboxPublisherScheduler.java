package org.example.promotionservice.scheduler;

import lombok.RequiredArgsConstructor;
import org.example.promotionservice.service.OutboxPublisherService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxPublisherScheduler {

    private final OutboxPublisherService outboxPublisherService;

    @Value("${outbox.publisher.batch-size:100}")
    private Integer batchSize;

    @Value("${outbox.publisher.topic-name:promotions-events}")
    private String topicName;

    @Scheduled(fixedDelayString = "${outbox.publisher.fixed-delay-ms:5000}")
    public void publishPendingEvents() {

        outboxPublisherService.publishPending(batchSize, topicName);
    }
}
