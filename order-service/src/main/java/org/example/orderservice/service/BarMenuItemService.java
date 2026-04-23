package org.example.orderservice.service;

import org.example.orderservice.entity.BarMenuItem;
import org.example.orderservice.exception.ValidationException;
import org.example.orderservice.repository.BarMenuItemRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BarMenuItemService {
    private final BarMenuItemRepository barMenuItemRepository;

    public BarMenuItemService(BarMenuItemRepository barMenuItemRepository) {
        this.barMenuItemRepository = barMenuItemRepository;
    }

    public List<BarMenuItem> findByBarId(UUID barId) {
        List<BarMenuItem> menuItems = barMenuItemRepository.findMenuByBarId(barId);
        if(menuItems.isEmpty()){
            throw new ValidationException("menuItems is empty");
        }
        return menuItems;
    }
}
