package com.mycompany.delivery;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Test {
    @org.junit.jupiter.api.Test
    public void test() {
        System.out.println(new BCryptPasswordEncoder().encode("admin"));
    }
}
