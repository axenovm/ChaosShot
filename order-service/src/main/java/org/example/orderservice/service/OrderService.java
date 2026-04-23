package org.example.orderservice.service;

import org.example.orderservice.DTO.request.OrderItemRequest;
import org.example.orderservice.DTO.request.OrderRequest;
import org.example.orderservice.DTO.response.PageResponse;
import org.example.orderservice.entity.BarMenuItem;
import org.example.orderservice.entity.Order;
import org.example.orderservice.entity.OrderItem;
import org.example.orderservice.exception.ResourceNotFoundException;
import org.example.orderservice.exception.ValidationException;
import org.example.orderservice.repository.BarMenuItemRepository;
import org.example.orderservice.repository.OrderItemRepository;
import org.example.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final BarMenuItemRepository barMenuItemRepository;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        BarMenuItemRepository barMenuItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.barMenuItemRepository = barMenuItemRepository;
    }

    public PageResponse searchOrders(UUID userId, UUID barId, int page, int size) {
        if (page < 0)
            page = 0;
        if (size < 1 || size > 50)
            size = 10;
        return orderRepository.searchOrders(userId, barId, page, size);
    }

    @Transactional
    public Order updateOrder(UUID id, OrderRequest request) {
        if (request.version() == null) {
            throw new ValidationException("Version is required for optimistic lock");
        }
        if (request.userId() == null || request.barId() == null || request.number() == null || request.number().isBlank()) {
            throw new ValidationException("userId, barId and number are required");
        }

        List<OrderItemRequest> items = request.items();
        if (items == null || items.isEmpty()) {
            throw new ValidationException("Order must contain at least one item");
        }
        for (OrderItemRequest line : items) {
            if (line.menuItemId() == null) {
                throw new ValidationException("Each item must have a menu item id");
            }
            if (line.quantity() == null || line.quantity() <= 0) {
                throw new ValidationException("Each item must have a positive quantity");
            }
        }

        Order existingOrder = findOrderById(id);
        Optional<Order> sameNumber = orderRepository.findOrdersByNumber(request.number());
        if (sameNumber.isPresent() && !sameNumber.get().id().equals(id)) {
            throw new ValidationException("Order number already exists");
        }

        List<UUID> distinctMenuIds = items.stream()
                .map(OrderItemRequest::menuItemId)
                .distinct()
                .toList();
        Map<UUID, BarMenuItem> menuById =
                barMenuItemRepository.findByBarIdAndMenuItemIds(request.barId(), distinctMenuIds);
        if (menuById.size() != distinctMenuIds.size()) {
            throw new ValidationException("Unknown menu item for this bar");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemRequest line : items) {
            BarMenuItem menu = menuById.get(line.menuItemId());
            totalAmount = totalAmount.add(menu.price().multiply(BigDecimal.valueOf(line.quantity())));
        }

        Order updatedCandidate = new Order(
                existingOrder.id(),
                totalAmount,
                existingOrder.createdAt(),
                request.userId(),
                request.barId(),
                request.number(),
                request.version());

        Order updated = orderRepository.updateOrder(updatedCandidate);

        orderItemRepository.deleteOrderItemsByOrderId(id);
        for (OrderItemRequest line : items) {
            BarMenuItem menu = menuById.get(line.menuItemId());
            OrderItem orderItem = new OrderItem(
                    UUID.randomUUID(),
                    menu.name(),
                    line.quantity(),
                    menu.price(),
                    id);
            if (!orderItemRepository.insertOrderItem(orderItem)) {
                throw new ValidationException("Order item persist failed");
            }
        }

        return updated;
    }

    @Transactional
    public Order createOrder(OrderRequest order) {
        Optional<Order> maybeOrder = orderRepository.findOrdersByNumber(order.number());
        if (maybeOrder.isPresent()) {
            throw new ValidationException("Order already exists");
        }

        List<OrderItemRequest> items = order.items();
        if (items == null || items.isEmpty()) {
            throw new ValidationException("Order must contain at least one item");
        }
        for (OrderItemRequest line : items) {
            if (line.menuItemId() == null) {
                throw new ValidationException("Each item must have a menu item id");
            }
            if (line.quantity() == null || line.quantity() <= 0) {
                throw new ValidationException("Each item must have a positive quantity");
            }
        }

        List<UUID> distinctMenuIds = items.stream()
                .map(OrderItemRequest::menuItemId)
                .distinct()
                .toList();
        Map<UUID, BarMenuItem> menuById =
                barMenuItemRepository.findByBarIdAndMenuItemIds(order.barId(), distinctMenuIds);
        if (menuById.size() != distinctMenuIds.size()) {
            throw new ValidationException("Unknown menu item for this bar");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemRequest line : items) {
            BarMenuItem menu = menuById.get(line.menuItemId());
            totalAmount = totalAmount.add(menu.price().multiply(BigDecimal.valueOf(line.quantity())));
        }

        Order newOrder = new Order(
                UUID.randomUUID(),
                totalAmount,
                LocalDateTime.now(),
                order.userId(),
                order.barId(),
                order.number(),
                0);

        if (!orderRepository.createOrder(newOrder)) {
            throw new ValidationException("Order created failed");
        }

        for (OrderItemRequest line : items) {
            BarMenuItem menu = menuById.get(line.menuItemId());
            OrderItem orderItem = new OrderItem(
                    UUID.randomUUID(),
                    menu.name(),
                    line.quantity(),
                    menu.price(),
                    newOrder.id());
            if (!orderItemRepository.insertOrderItem(orderItem)) {
                throw new ValidationException("Order item persist failed");
            }
        }

        return newOrder;
    }

    public Order findOrderById(UUID id) {
        Optional<Order> order = orderRepository.findOrderById(id);
        if (order.isEmpty())
            throw new ResourceNotFoundException("Order not found");
        else
            return order.get();
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAllOrders();
    }

    public List<Order> findOrdersByUserId(UUID user_id) {
        return orderRepository.findOrdersByUserId(user_id);
    }

    @Transactional
    public void deleteOrderById(UUID id) {
        findOrderById(id);
        orderItemRepository.deleteOrderItemsByOrderId(id);
        boolean deleted = orderRepository.deleteOrderById(id);
        if (!deleted) {
            throw new ResourceNotFoundException("Order not found");
        }
    }
}
