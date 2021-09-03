package com.mycompany.delivery.service;

import com.mycompany.delivery.entity.Order;
import com.mycompany.delivery.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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

    public int calculateCost(Order order) {
        return (int) (order.getRoute().getLength() / 10 + order.getLength() + order.getWidth() + order.getHeight() + order.getWeight());
    }

    public Page<Order> findPaginatedByUserId(long userId, int currentPage, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, sort);
        return orderRepository.findAllByUserId(userId, pageable);
    }

    public Page<Order> findPaginated(int currentPage, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, sort);
        return orderRepository.findAll(pageable);
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order with id " + id + " was not found"));
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public List<Order> findAllByUserId(long userId) {
        return orderRepository.findAll().stream().filter((o) -> o.getUser().getId() == userId).collect(Collectors.toList());
    }
}
