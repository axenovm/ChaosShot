package org.example.orderservice.DTO.response;

import org.example.orderservice.entity.Order;

import java.util.List;

public record PageResponse (
        List<Order> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext){

}
