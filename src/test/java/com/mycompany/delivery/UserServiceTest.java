package com.mycompany.delivery;

import com.mycompany.delivery.entity.User;
import com.mycompany.delivery.service.RoleService;
import com.mycompany.delivery.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Collections;

@SpringBootTest
@Transactional
public class UserServiceTest {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserServiceTest(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    private User testUser;

    @BeforeEach
    public void init() {
        testUser = User.builder()
                .username("testUser")
                .email("testUser@mail.com")
                .password("testUser")
                .balance(0)
                .authorities(Collections.singleton(roleService.findByName("ROLE_USER")))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();
    }

    @Test
    public void saveUser_ValidUser_returnedUserEqualsToValidUser() {
        User resultUser = userService.saveUser(testUser);

        Assertions.assertEquals(resultUser, testUser);
    }
}
