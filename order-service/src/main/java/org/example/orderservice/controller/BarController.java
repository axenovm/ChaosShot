package org.example.orderservice.controller;

import org.example.orderservice.entity.Bar;
import org.example.orderservice.service.BarService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bar")
public class BarController {
    public final BarService barService;

    public BarController(BarService barService) {
        this.barService = barService;
    }

    @GetMapping("{id}")
    public Bar getBar(@PathVariable UUID id) {
        return barService.findBarById(id);
    }

    @GetMapping
    public List<Bar> getAllBars() {
        return barService.findAllBars();
    }
}
