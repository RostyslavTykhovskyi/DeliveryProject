package com.mycompany.delivery.controller;

import com.mycompany.delivery.entity.Order;
import com.mycompany.delivery.entity.Status;
import com.mycompany.delivery.service.OrderService;
import com.mycompany.delivery.service.RouteService;
import com.mycompany.delivery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {
    private final RouteService routeService;
    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public AdminController(RouteService routeService, OrderService orderService, UserService userService) {
        this.routeService = routeService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @RequestMapping("/admin")
    public String getAdminPage() {
        return "redirect:/admin/orders";
    }

    @RequestMapping("admin/orders")
    public String getAdminOrdersPage(Model model) {
        model.addAttribute("orders", orderService.findAll());
        return "admin";
    }

    @PostMapping("/admin/orders")
    public String generateReceipt(@RequestParam(name = "id") long id) {
        Order order = orderService.findById(id);
        order.setStatus(Status.AWAITING_PAYMENT);
        orderService.saveOrder(order);
        return "redirect:/admin/orders";
    }
}
