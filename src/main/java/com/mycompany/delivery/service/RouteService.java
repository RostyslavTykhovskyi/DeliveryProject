package com.mycompany.delivery.service;

import com.mycompany.delivery.entity.Route;
import com.mycompany.delivery.repository.RouteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {
    Logger log = LoggerFactory.getLogger(RouteService.class);

    private final RouteRepository routeRepository;

    @Autowired
    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public Page<Route> findPaginated(int currentPage, int pageSize, String sortField, String sortDirection) {
        log.info("Finding paginated routes");
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, sort);
        return routeRepository.findAll(pageable);
    }

    public Route saveRoute(Route route) {
        log.info("Saving routes");
        return routeRepository.save(route);
    }

    public Route findById(long id) {
        log.info("Finding route with id = " + id);
        return routeRepository.findById(id).orElseThrow(() -> new RuntimeException("Route with id " + id + " was not found"));
    }

    public List<Route> findAll() {
        log.info("Finding all routes");
        return routeRepository.findAll();
    }

    public void deleteRoute(long id) {
        log.info("Deleting route with id = " + id);
        routeRepository.deleteById(id);
    }
}
