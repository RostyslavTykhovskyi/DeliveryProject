package com.mycompany.delivery.service;

import com.mycompany.delivery.entity.Order;
import com.mycompany.delivery.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
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
