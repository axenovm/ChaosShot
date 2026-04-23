package org.example.orderservice.controller;

import org.example.orderservice.entity.OrderItem;
import org.example.orderservice.service.OrderItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
//@RequestMapping("order-item")
public class OrderItemController {
    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @GetMapping("/order-item")
    public List<OrderItem> findAllItems() {
        return orderItemService.findAllItems();
    }

    @GetMapping("/order-item/{id}")
    public OrderItem findItemById(@PathVariable UUID id) {
        return orderItemService.findItemById(id);
    }

    @GetMapping("{order_id}/order-item")
    public List<OrderItem> findAllItemsByOrderId(@PathVariable UUID order_id) {
        return orderItemService.findItemsByOrderId(order_id);
    }
}
