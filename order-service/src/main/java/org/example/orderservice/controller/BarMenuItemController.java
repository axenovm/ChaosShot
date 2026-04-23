package org.example.orderservice.controller;

import org.example.orderservice.entity.BarMenuItem;
import org.example.orderservice.service.BarMenuItemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/menu")
public class BarMenuItemController {
    private final BarMenuItemService barMenuItemService;

    public BarMenuItemController(BarMenuItemService barMenuItemService) {
        this.barMenuItemService = barMenuItemService;
    }

    @GetMapping("/{id}")
    public List<BarMenuItem> findMenuByBarId(@PathVariable UUID id) {
        return barMenuItemService.findByBarId(id);
    }
}
