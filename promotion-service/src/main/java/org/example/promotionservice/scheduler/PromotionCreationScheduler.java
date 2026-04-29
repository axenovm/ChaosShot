package org.example.promotionservice.scheduler;

import lombok.RequiredArgsConstructor;
import org.example.promotionservice.DTO.request.PromotionRequest;
import org.example.promotionservice.exception.ValidationException;
import org.example.promotionservice.service.PromotionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class PromotionCreationScheduler {

    private final PromotionService promotionService;
    private final Logger log = Logger.getLogger(PromotionCreationScheduler.class.getName());

    @Scheduled(cron = "${promotion.creation.cron:0 0 */12 * * *}")
    public void createPromotionBySchedule() {
        LocalDateTime now = LocalDateTime.now();
        String name = buildSeasonPromotionName(now);
        try {
            promotionService.createPromotion(new PromotionRequest(name));
            log.info("Scheduled promotion created: " + name);
        } catch (ValidationException e) {
            log.log(Level.INFO, "Scheduled promotion skipped: " + e.getMessage());
        } catch (RuntimeException e) {
            log.log(Level.WARNING, "Scheduled promotion creation failed", e);
        }
    }

    private String buildSeasonPromotionName(LocalDateTime now) {
        String seasonPrefix = switch (now.getMonth()) {
            case DECEMBER, JANUARY, FEBRUARY -> "Зимняя";
            case MARCH, APRIL, MAY -> "Весенняя";
            case JUNE, JULY, AUGUST -> "Летняя";
            case SEPTEMBER, OCTOBER, NOVEMBER -> "Осенняя";
        };
        return seasonPrefix + " супер акция " + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
