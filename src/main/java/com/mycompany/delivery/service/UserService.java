package com.mycompany.delivery.service;

import com.mycompany.delivery.entity.Order;
import com.mycompany.delivery.entity.Status;
import com.mycompany.delivery.entity.User;
import com.mycompany.delivery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final OrderService orderService;

    @Autowired
    public UserService(UserRepository userRepository, OrderService orderService) {
        this.userRepository = userRepository;
        this.orderService = orderService;
    }

    @Transactional
    public void payForOrder(Order order) {
        User user = userRepository.findByUsername(order.getUser().getUsername()).orElseThrow(() -> new UsernameNotFoundException("user not found"));
        user.setBalance(user.getBalance() - order.getCost());
        order.setStatus(Status.PAID);
        userRepository.save(user);
        orderService.saveOrder(order);
    }

    public User findById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("user not found"));
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with name " + username + " was not found"));
    }
}
