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
@RequestMapping("/admin")
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

    @RequestMapping
    public String getAdminPage() {
        return "admin";
    }

    @RequestMapping("/orders")
    public String getAdminOrdersPage(Model model, @RequestParam(name = "page", required = false, defaultValue = "1") int page) {
        model.addAttribute("orderPage", orderService.findPaginated(page, 5));
        return "orders";
    }

    @PostMapping("/orders")
    public String generateReceipt(@RequestParam(name = "id") long id) {
        Order order = orderService.findById(id);
        order.setStatus(Status.AWAITING_PAYMENT);
        orderService.saveOrder(order);
        return "redirect:/admin/orders";
    }

    @RequestMapping("/users")
    public String getAdminUsersPage(Model model, @RequestParam(name = "page", required = false, defaultValue = "1") int page) {
        model.addAttribute("userPage", userService.findPaginated(page, 5));
        return "users";
    }

    @RequestMapping("/routes")
    public String getAdminRoutesPage(Model model, @RequestParam(name = "page", required = false, defaultValue = "1") int page) {
        model.addAttribute("routePage", routeService.findPaginated(page, 5));
        return "routes";
    }
}
