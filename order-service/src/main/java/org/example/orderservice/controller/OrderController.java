package org.example.orderservice.controller;

import org.example.orderservice.DTO.request.OrderRequest;
import org.example.orderservice.DTO.response.PageResponse;
import org.example.orderservice.entity.Order;
import org.springframework.http.HttpStatus;
import org.example.orderservice.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order createOrder(@RequestBody OrderRequest order)
    {
        return orderService.createOrder(order);
    }

    @GetMapping("/search")
    public PageResponse searchOrders(@RequestParam(required = false) UUID userId,
                                     @RequestParam(required = false) UUID barId,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size)
    {
        return orderService.searchOrders(userId, barId, page, size);
    }

    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable UUID id, @RequestBody OrderRequest request) {
        return orderService.updateOrder(id, request);
    }

    @GetMapping("/{id}")
    public Order findOrderById(@PathVariable UUID id) {
        return orderService.findOrderById(id);
    }

    @GetMapping("")
    public List<Order> findAllOrders() {
        return orderService.findAllOrders();
    }

    @GetMapping("/user/{id}")
    public List<Order> findAllOrdersByUserId(@PathVariable UUID id) {
        return orderService.findOrdersByUserId(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrderById(@PathVariable UUID id) {
        orderService.deleteOrderById(id);
    }
}
