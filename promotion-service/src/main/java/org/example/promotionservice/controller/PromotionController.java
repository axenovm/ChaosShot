package org.example.promotionservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.promotionservice.DTO.request.PromotionRequest;
import org.example.promotionservice.DTO.response.PromotionResponse;
import org.example.promotionservice.service.PromotionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/promotion")
public class PromotionController {
    private final PromotionService promotionService;

    @GetMapping("/{id}")
    public PromotionResponse findPromotionById(@PathVariable UUID id) {
        return promotionService.findById(id);
    }

    @GetMapping
    public List<PromotionResponse> findAllPromotions() {
        return promotionService.findAll();
    }

    @PostMapping
    public PromotionResponse createPromotion(@RequestBody PromotionRequest promotionRequest) {
        return promotionService.createPromotion(promotionRequest);
    }

    @PutMapping("/{id}")
    public PromotionResponse updatePromotion(@RequestBody PromotionRequest promotionRequest,
                                             @PathVariable UUID id) {
        return promotionService.updatePromotion(id, promotionRequest);
    }

    @DeleteMapping("/{id}")
    public void deletePromotion(@PathVariable UUID id) {
        promotionService.deletePromotion(id);
    }
}
