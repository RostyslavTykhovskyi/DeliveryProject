package com.mycompany.delivery.service;

import com.mycompany.delivery.entity.Route;
import com.mycompany.delivery.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {
    private final RouteRepository routeRepository;

    @Autowired
    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public Route saveRoute(Route route) {
        return routeRepository.save(route);
    }

    public List<Route> findAll() {
        return routeRepository.findAll();
    }
}
