package com.mycompany.delivery.controller;

import com.mycompany.delivery.entity.Order;
import com.mycompany.delivery.entity.Status;
import com.mycompany.delivery.entity.User;
import com.mycompany.delivery.service.OrderService;
import com.mycompany.delivery.service.RouteService;
import com.mycompany.delivery.validator.CalculateOrderValidation;
import com.mycompany.delivery.validator.FullOrderValidation;
import com.mycompany.delivery.validator.ValidationConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/order")
public class OrderController {
    private static Logger log = LoggerFactory.getLogger(AdminController.class);

    private final RouteService routeService;
    private final OrderService orderService;

    @Autowired
    public OrderController(RouteService routeService, OrderService orderService) {
        this.routeService = routeService;
        this.orderService = orderService;
    }

    @GetMapping
    public String getMainPage(@RequestAttribute(name = "routeId") Optional<Long> routeId, Model model) {
        Order order = new Order();

        routeId.ifPresent((id) -> order.setRoute(routeService.findById(id)));

        model.addAttribute("order", order);
        model.addAttribute("routes", routeService.findAll());
        model.addAttribute("routeId", routeId);
        model.addAttribute("minDimension", ValidationConstants.MIN_DIMENSION);
        model.addAttribute("maxDimension", ValidationConstants.MAX_DIMENSION);
        model.addAttribute("minWeight", ValidationConstants.MIN_WEIGHT);
        model.addAttribute("maxWeight", ValidationConstants.MAX_WEIGHT);
        model.addAttribute("minDate", LocalDate.now().plusDays(ValidationConstants.MIN_DAYS_AFTER_ORDER));
        model.addAttribute("maxDate", LocalDate.now().plusDays(ValidationConstants.MAX_DAYS_AFTER_ORDER));

        return "order";
    }

    @PostMapping(params = "calculate")
    public String calculateCost(@ModelAttribute("order") @Validated(CalculateOrderValidation.class) Order order, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("routes", routeService.findAll());
            model.addAttribute("minDimension", ValidationConstants.MIN_DIMENSION);
            model.addAttribute("maxDimension", ValidationConstants.MAX_DIMENSION);
            model.addAttribute("minWeight", ValidationConstants.MIN_WEIGHT);
            model.addAttribute("maxWeight", ValidationConstants.MAX_WEIGHT);
            model.addAttribute("minDate", LocalDate.now().plusDays(ValidationConstants.MIN_DAYS_AFTER_ORDER));
            model.addAttribute("maxDate", LocalDate.now().plusDays(ValidationConstants.MAX_DAYS_AFTER_ORDER));

            return "order";
        }

        model.addAttribute("cost", orderService.calculateCost(order));
        model.addAttribute("routes", routeService.findAll());

        return "order";
    }

    @PostMapping(params = "makeOrder")
    public String makeOrder(@ModelAttribute("order") @Validated(FullOrderValidation.class) Order order, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("routes", routeService.findAll());
            model.addAttribute("minDimension", ValidationConstants.MIN_DIMENSION);
            model.addAttribute("maxDimension", ValidationConstants.MAX_DIMENSION);
            model.addAttribute("minWeight", ValidationConstants.MIN_WEIGHT);
            model.addAttribute("maxWeight", ValidationConstants.MAX_WEIGHT);
            model.addAttribute("minDate", LocalDate.now().plusDays(ValidationConstants.MIN_DAYS_AFTER_ORDER));
            model.addAttribute("maxDate", LocalDate.now().plusDays(ValidationConstants.MAX_DAYS_AFTER_ORDER));

            return "order";
        }

        order.setStatus(Status.PROCESSING);
        order.setCost(orderService.calculateCost(order));
        order.setUser((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        orderService.saveOrder(order);

        return "redirect:/order";
    }
}
