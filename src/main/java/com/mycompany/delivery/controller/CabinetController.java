package com.mycompany.delivery.controller;

import com.mycompany.delivery.entity.User;
import com.mycompany.delivery.service.OrderService;
import com.mycompany.delivery.service.RouteService;
import com.mycompany.delivery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cabinet")
public class CabinetController {
    private final RouteService routeService;
    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public CabinetController(RouteService routeService, OrderService orderService, UserService userService) {
        this.routeService = routeService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @RequestMapping
    public String getCabinetPage(Model model, @RequestParam(name = "page", required = false, defaultValue = "1") int page) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        model.addAttribute("orderPage", orderService.findPaginatedByUserId(user.getId(), page, 5));
        model.addAttribute("balance", userService.findById(user.getId()).getBalance());
        return "cabinet";
    }

    @PostMapping
    public String payForOrder(@RequestParam(name = "id") long id) {
        userService.payForOrder(orderService.findById(id));
        return "redirect:/cabinet";
    }

    @PostMapping("/topup")
    public String topUp(@RequestParam(name = "amount") int amount) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setBalance(user.getBalance() + amount);
        userService.saveUser(user);
        return "redirect:/cabinet";
    }
}
