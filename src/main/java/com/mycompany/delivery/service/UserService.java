package com.mycompany.delivery.service;

import com.mycompany.delivery.entity.Order;
import com.mycompany.delivery.entity.Status;
import com.mycompany.delivery.entity.User;
import com.mycompany.delivery.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserTransactional userTransactional;

    public UserService(UserRepository userRepository, UserTransactional userTransactional) {
        this.userRepository = userRepository;
        this.userTransactional = userTransactional;
    }

    public void payForOrder(Order order) {
        log.info("Paying for order with id = " + order.getId());
        userTransactional.payForOrder(order);
    }

    public void topUpBalance(long userId, int amount) {
        log.info("User with id = " + userId + " top up his balance");
        userTransactional.topUpBalance(userId, amount);
    }

    public Page<User> findPaginated(int currentPage, int pageSize, String sortField, String sortDirection) {
        log.info("Finding paginated users");
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, sort);
        return userRepository.findAll(pageable);
    }

    public User findById(long id) {
        log.info("Finding user by id = " + id);
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User with id " + id + " was not found"));
    }

    public User saveUser(User user) {
        log.info("Saving user");
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with name " + username + " was not found"));
    }
}
