package com.mycompany.delivery.controller;

import com.mycompany.delivery.entity.Order;
import com.mycompany.delivery.entity.Status;
import com.mycompany.delivery.entity.User;
import com.mycompany.delivery.service.OrderService;
import com.mycompany.delivery.service.RouteService;
import com.mycompany.delivery.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.time.LocalDate;

@Controller
public class MainController {
    private static Logger log = LoggerFactory.getLogger(AdminController.class);

    private final RouteService routeService;
    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public MainController(RouteService routeService, OrderService orderService, UserService userService) {
        this.routeService = routeService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @RequestMapping("/")
    public String getMainPage(Model model) {
        model.addAttribute("order", new Order());
        model.addAttribute("routes", routeService.findAll());
        model.addAttribute("minDate", LocalDate.now().plusDays(3));
        return "main";
    }

    @PostMapping(value = "/", params = "calculate")
    public String calculateCost(@ModelAttribute("order") Order order, Model model) {
        model.addAttribute("cost", orderService.calculateCost(order));
        model.addAttribute("routes", routeService.findAll());
        return "main";
    }

    @PostMapping(value = "/", params = "makeOrder")
    public String makeOrder(@ModelAttribute("order") @Valid Order order, BindingResult bindingResult, Model model, @RequestParam(name = "lang", required = false) String lang) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("routes", routeService.findAll());
            model.addAttribute("minDate", LocalDate.now().plusDays(3));
            return "main";
        }

        order.setStatus(Status.PROCESSING);
        order.setCost(orderService.calculateCost(order));
        order.setUser((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        orderService.saveOrder(order);
        return "redirect:/";
    }

    @RequestMapping("/login")
    public String getLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        model.addAttribute("error", error != null);
        return "login";
    }
}
