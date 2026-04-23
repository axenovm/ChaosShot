package org.example.promotionservice.service;

import lombok.RequiredArgsConstructor;
import org.example.promotionservice.DTO.request.PromotionRequest;
import org.example.promotionservice.DTO.response.PromotionResponse;
import org.example.promotionservice.entity.Outbox;
import org.example.promotionservice.entity.Promotion;
import org.example.promotionservice.exception.ResourceNotFoundException;
import org.example.promotionservice.exception.ValidationException;
import org.example.promotionservice.repository.OutboxRepository;
import org.example.promotionservice.repository.PromotionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PromotionService {
    private final PromotionRepository promotionRepository;
    private final OutboxRepository outboxRepository;

    public PromotionResponse findById(UUID id) {
        Optional<Promotion> promotion = promotionRepository.findById(id);
        if (promotion.isEmpty()) {
            throw new ResourceNotFoundException("Promotion with id " + id + " not found");
        }
        else
            return convertToResponse(promotion.get());
    }

    public List<PromotionResponse> findAll() {
        return promotionRepository.findAll().stream().map(this::convertToResponse).toList();
    }

    public PromotionResponse convertToResponse(Promotion promotion) {
        return new PromotionResponse(
                promotion.id(),
                promotion.name(),
                promotion.discountPercent(),
                promotion.startsAt(),
                promotion.endsAt(),
                promotion.isActive());
    }

    @Transactional
    public PromotionResponse createPromotion(PromotionRequest promotionRequest) {
        Optional<Promotion> maybePromotion = promotionRepository.findByName(promotionRequest.name());
        if (maybePromotion.isPresent()) {
            throw new ValidationException("Promotion with name " + promotionRequest.name() + " already exists");
        }

        if (promotionRequest.name().isEmpty()) {
            throw new ValidationException("Promotion name is empty");
        }

        Promotion promotion = new Promotion(
                UUID.randomUUID(),
                promotionRequest.name(),
                (int) (Math.random() * 81) + 10,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(new Random().nextInt(30) + 1),
                true);

        if (!promotionRepository.create(promotion)) {
            throw new ValidationException("Promotion persist failed");
        }
        if (!outboxRepository.create(new Outbox(
                UUID.randomUUID(),
                promotion.id(),
                "PROMOTION_CREATED",
                LocalDateTime.now(),
                false))) {
            throw new ValidationException("Outbox persist failed");
        }

        return convertToResponse(promotion);
    }

    public void deletePromotion(UUID id) {
        if(!promotionRepository.deleteById(id))
            throw new ResourceNotFoundException("Promotion with id " + id + " not found");
    }

    public PromotionResponse updatePromotion(UUID id, PromotionRequest promotionRequest) {
        Promotion promotion = promotionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Promotion to update with id " + id + " not found"));

        if (promotionRequest.name().isEmpty()) {
            throw new ValidationException("Promotion name is empty");
        }

        Promotion promotionToUpdate = new  Promotion(
                promotion.id(),
                promotionRequest.name(),
                promotion.discountPercent(),
                promotion.startsAt(),
                promotion.endsAt(),
                promotion.isActive());

        if(!promotionRepository.update(promotionToUpdate))
        {
            throw new ValidationException("Promotion updated failed");
        }
        return convertToResponse(promotionToUpdate);
    }

}
