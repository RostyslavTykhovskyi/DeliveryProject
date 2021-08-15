package com.mycompany.delivery.repository;

import com.mycompany.delivery.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {
}
