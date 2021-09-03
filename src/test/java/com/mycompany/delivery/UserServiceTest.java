package com.mycompany.delivery;

import com.mycompany.delivery.entity.User;
import com.mycompany.delivery.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceTest {
    private final UserService userService;

    @Autowired
    public UserServiceTest(UserService userService) {
        this.userService = userService;
    }

    @Test
    public void SaveUser_ValidUser_returnSavedUser() {
        User testUser = User.builder()
                .username("testUser")
                .email("testUser@mail.com")
                .password("testUser")
                .balance(0)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();

        User resultUser = userService.saveUser(testUser);

    }
}
