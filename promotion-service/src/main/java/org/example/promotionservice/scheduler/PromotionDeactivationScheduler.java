package org.example.promotionservice.scheduler;

import lombok.RequiredArgsConstructor;
import org.example.promotionservice.service.PromotionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class PromotionDeactivationScheduler {
    private final PromotionService promotionService;
    private final Logger log = Logger.getLogger(PromotionDeactivationScheduler.class.getName());

    @Scheduled(fixedDelayString = "${promotion.deactivation.fixed-delay-ms:60000}")
    public void deactivateExpiredPromotions() {
        try {
            promotionService.deactivateExpiredPromotions();
        } catch (RuntimeException e) {
            log.log(Level.WARNING, "Scheduled promotion deactivation failed", e);
        }
    }
}
