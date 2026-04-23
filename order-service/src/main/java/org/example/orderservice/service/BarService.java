package org.example.orderservice.service;

import org.example.orderservice.entity.Bar;
import org.example.orderservice.exception.ResourceNotFoundException;
import org.example.orderservice.repository.BarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BarService {
    public final BarRepository barRepository;

    public BarService(BarRepository barRepository) {
        this.barRepository = barRepository;
    }

    public Bar findBarById(UUID bar_id) {
        Optional<Bar> bar = barRepository.findBarById(bar_id);
        if (bar.isEmpty()) {
            throw new ResourceNotFoundException("Bar not found");
        } else
            return bar.get();
    }

    public List<Bar> findAllBars() {
        return barRepository.findAllBars();
    }
}
