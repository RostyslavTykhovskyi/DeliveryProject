package com.mycompany.delivery.service;

import com.mycompany.delivery.entity.Order;
import com.mycompany.delivery.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public int calculateCost(Order order) {
        log.info("Calculating order cost");
        return (int) (order.getRoute().getLength() / 10 + order.getLength() + order.getWidth() + order.getHeight() + order.getWeight());
    }

    public Page<Order> findPaginatedByUserId(long userId, int currentPage, int pageSize, String sortField, String sortDirection) {
        log.info("Finding paginated orders with user id = " + userId);
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, sort);
        return orderRepository.findAllByUserId(userId, pageable);
    }

    public Page<Order> findPaginated(int currentPage, int pageSize, String sortField, String sortDirection) {
        log.info("Finding paginated orders");
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, sort);
        return orderRepository.findAll(pageable);
    }

    public Order saveOrder(Order order) {
        log.info("Saving order");
        return orderRepository.save(order);
    }

    public Order findById(Long id) {
        log.info("Finding order with id = " + id);
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order with id " + id + " was not found"));
    }
}
