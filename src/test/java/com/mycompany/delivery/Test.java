package com.mycompany.delivery;

import com.mycompany.delivery.entity.Role;

public class Test {
    @org.junit.jupiter.api.Test
    public void test() {
        Role.RoleBuilder builder = Role.builder();

        Role role = builder.id(10).name("name").build();

        System.out.println(role.getName());

        builder.name("hello");

        System.out.println(role.getName());
    }
}
