package com.mycompany.delivery.service;

import com.mycompany.delivery.entity.Order;
import com.mycompany.delivery.entity.Status;
import com.mycompany.delivery.entity.User;
import com.mycompany.delivery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class UserTransactional {
    private final UserRepository userRepository;
    private final OrderService orderService;

    @Autowired
    public UserTransactional(UserRepository userRepository, OrderService orderService) {
        this.userRepository = userRepository;
        this.orderService = orderService;
    }

    @Transactional
    public void payForOrder(Order order) {
        User user = userRepository.findById(order.getUser().getId()).orElseThrow(
                () -> new RuntimeException("User with id " + order.getUser().getId() + " was not found")
        );

        user.setBalance(user.getBalance() - order.getCost());
        order.setStatus(Status.PAID);

        userRepository.save(user);
        orderService.saveOrder(order);
    }

    @Transactional
    public void topUpBalance(long userId, int amount) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("User with id " + userId + " was not found")
        );

        user.setBalance(user.getBalance() + amount);

        userRepository.save(user);
    }
}
