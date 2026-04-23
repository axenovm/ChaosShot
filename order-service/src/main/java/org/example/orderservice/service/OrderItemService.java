package org.example.orderservice.service;

import org.example.orderservice.entity.Bar;
import org.example.orderservice.entity.OrderItem;
import org.example.orderservice.exception.ResourceNotFoundException;
import org.example.orderservice.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public OrderItem findItemById(UUID item_id) {
        Optional<OrderItem> item = orderItemRepository.findOrderItemById(item_id);
        if (item.isEmpty()) {
            throw new ResourceNotFoundException("Order item not found");
        } else
            return item.get();
    }

    public List<OrderItem> findItemsByOrderId(UUID order_id) {
        return orderItemRepository.findOrderItemByOrderId(order_id);
    }

    public List<OrderItem> findAllItems() {
        return orderItemRepository.findAllItems();
    }
}
