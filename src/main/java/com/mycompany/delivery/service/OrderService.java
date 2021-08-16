package com.mycompany.delivery.service;

import com.mycompany.delivery.entity.Order;
import com.mycompany.delivery.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Page<Order> findPaginatedByUserId(long userId, int currentPage, int pageSize) {
        int startItem = (currentPage - 1) * pageSize;
        List<Order> orders = findAllByUserId(userId).stream()
                .sorted(Comparator.comparingLong(Order::getId).reversed())
                .collect(Collectors.toList());
        List<Order> list;

        if (orders.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, orders.size());
            list = orders.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage - 1, pageSize), orders.size());
    }

    public Page<Order> findPaginated(int currentPage, int pageSize) {
        final Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("id").descending());
        return orderRepository.findAll(pageable);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public List<Order> findAllByUserId(long userId) {
        return orderRepository.findAll().stream().filter((o) -> o.getUser().getId() == userId).collect(Collectors.toList());
    }

    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("not found"));
    }

    public int calculateCost(Order order) {
        return order.getRoute().getLength() / 10 + order.getLength() + order.getWidth() + order.getHeight() + order.getWeight();
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }
}
