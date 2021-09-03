package com.mycompany.delivery.controller;

import com.mycompany.delivery.entity.Order;
import com.mycompany.delivery.entity.Route;
import com.mycompany.delivery.entity.Status;
import com.mycompany.delivery.entity.User;
import com.mycompany.delivery.service.OrderService;
import com.mycompany.delivery.service.RoleService;
import com.mycompany.delivery.service.RouteService;
import com.mycompany.delivery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final RouteService routeService;
    private final OrderService orderService;
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(RouteService routeService, OrderService orderService, UserService userService, RoleService roleService) {
        this.routeService = routeService;
        this.orderService = orderService;
        this.userService = userService;
        this.roleService = roleService;
    }

    @RequestMapping
    public String getAdminPage() {
        return "admin/admin";
    }

    @RequestMapping("/orders")
    public String getAdminOrdersPage(
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "sortField", required = false, defaultValue = "createdOn") String sortField,
            @RequestParam(name = "sortDirection", required = false, defaultValue = "desc") String sortDirection,
            Model model
    ) {
        model.addAttribute("orderPage", orderService.findPaginated(page, 5, sortField, sortDirection));
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDir", sortDirection.equals("asc") ? "desc" : "asc");
        return "admin/orders";
    }

    @PostMapping("/orders")
    public String generateReceipt(@RequestParam("id") long id) {
        Order order = orderService.findById(id);
        order.setStatus(Status.AWAITING_PAYMENT);
        orderService.saveOrder(order);
        return "redirect:/admin/orders";
    }

    @RequestMapping("/users")
    public String getAdminUsersPage(
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "sortField", required = false, defaultValue = "id") String sortField,
            @RequestParam(name = "sortDirection", required = false, defaultValue = "desc") String sortDirection,
            Model model
    ) {
        model.addAttribute("userPage", userService.findPaginated(page, 5, sortField, sortDirection));
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDir", sortDirection.equals("asc") ? "desc" : "asc");
        return "admin/users";
    }

    @GetMapping("/users/{id}")
    public String getUserFormPage(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "admin/user_form";
    }

    @PostMapping("/users/{id}")
    public String saveUser(@PathVariable("id") long id, @ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/user_form";
        }

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        userService.saveUser(user);
        return "redirect:/admin/users";
    }

    @RequestMapping("/routes")
    public String getAdminRoutesPage(
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "sortField", required = false, defaultValue = "id") String sortField,
            @RequestParam(name = "sortDirection", required = false, defaultValue = "desc") String sortDirection,
            Model model
    ) {
        model.addAttribute("routePage", routeService.findPaginated(page, 5, sortField, sortDirection));
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDir", sortDirection.equals("asc") ? "desc" : "asc");
        return "admin/routes";
    }

    @GetMapping("/routes/{id}")
    public String getRouteFormPage(@PathVariable("id") long id, Model model) {
        model.addAttribute("route", routeService.findById(id));
        return "admin/route_form";
    }

    @PostMapping("/routes/{id}")
    public String saveRoute(@PathVariable("id") long id, @ModelAttribute("route") @Valid Route route, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/route_form";
        }

        routeService.saveRoute(route);
        return "redirect:/admin/routes";
    }

    @GetMapping("/deleteRoute/{id}")
    public String deleteRoute(@PathVariable("id") long id) {
        routeService.deleteRoute(id);
        return "redirect:/admin/routes";
    }
}
