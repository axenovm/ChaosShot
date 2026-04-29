package org.example.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.DTO.request.BarMenuItemUpdateRequest;
import org.example.orderservice.entity.Bar;
import org.example.orderservice.entity.BarMenuItem;
import org.example.orderservice.entity.OrderPromotion;
import org.example.orderservice.entity.PromotionEvent;
import org.example.orderservice.exception.ResourceNotFoundException;
import org.example.orderservice.repository.BarMenuItemRepository;
import org.example.orderservice.repository.BarRepository;
import org.example.orderservice.repository.OrderPromotionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class OrderPromotionService {
    private final BarMenuItemRepository barMenuItemRepository;
    private final BarRepository barRepository;
    private final OrderPromotionRepository orderPromotionRepository;
    private final Logger log = Logger.getLogger(OrderPromotionService.class.getName());

    public void deactivatePromotion(UUID promotionId)
    {
        OrderPromotion orderPromotion = orderPromotionRepository.findByPromotionId(promotionId).orElseThrow(
                ()->new ResourceNotFoundException("Order Promotion not found"));
        orderPromotionRepository.deactivatePromotionByPromotionId(promotionId);
        BarMenuItem barMenuItem = barMenuItemRepository.findById(orderPromotion.shotId()).orElseThrow(
                (()->new ResourceNotFoundException("Bar MenuItem not found")));

        BigDecimal originalPrice = barMenuItem.price()
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(100 - orderPromotion.discountPercent()), 2, RoundingMode.HALF_UP);

        barMenuItemRepository.updateItemPrice(new BarMenuItemUpdateRequest(
                orderPromotion.shotId(),
                originalPrice,
                0));
    }

    public void addPromotion(PromotionEvent promotionEvent) {
        List<Bar> bars  = barRepository.findAllBars();
        for (Bar bar : bars) {
            List<BarMenuItem> barMenuItems = barMenuItemRepository.findMenuByBarId(bar.id());
            if (barMenuItems.isEmpty()) {
                log.warning("Promotion skipped for bar without menu items: " + bar.id());
                continue;
            }
            BarMenuItem barMenuItemToPromo = barMenuItems.get((int) (Math.random() * barMenuItems.size()));
            List<OrderPromotion> activePromotionsOnShot = orderPromotionRepository.findActiveByShotId(barMenuItemToPromo.id());
            for (OrderPromotion activePromotion : activePromotionsOnShot) {
                deactivatePromotion(activePromotion.promotionId());
            }

            BigDecimal discountMultiplier = BigDecimal.valueOf(100 - promotionEvent.discountPercent())
                    .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
            BigDecimal newPrice = barMenuItemToPromo.price()
                    .multiply(discountMultiplier)
                    .setScale(2, RoundingMode.HALF_UP);
            barMenuItemRepository.updateItemPrice(new BarMenuItemUpdateRequest(
                    barMenuItemToPromo.id(),
                    newPrice,
                    promotionEvent.discountPercent()));

            OrderPromotion orderPromotion = new OrderPromotion(
                    promotionEvent.promotionId(),
                    barMenuItemToPromo.id(),
                    promotionEvent.eventId(),
                    promotionEvent.name(),
                    promotionEvent.discountPercent(),
                    promotionEvent.startsAt(),
                    promotionEvent.endsAt(),
                    promotionEvent.active()
            );
            orderPromotionRepository.upsert(orderPromotion);
        }
    }
}
