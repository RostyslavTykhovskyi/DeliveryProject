package com.mycompany.delivery.service;

import com.mycompany.delivery.entity.Role;
import com.mycompany.delivery.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }
}
