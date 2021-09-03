package com.mycompany.delivery.controller;

import com.mycompany.delivery.entity.User;
import com.mycompany.delivery.service.OrderService;
import com.mycompany.delivery.service.RouteService;
import com.mycompany.delivery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cabinet")
public class CabinetController {
    private final RouteService routeService;
    private final OrderService orderService;
    private final UserService userService;

    public CabinetController(RouteService routeService, OrderService orderService, UserService userService) {
        this.routeService = routeService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @RequestMapping
    public String getCabinetPage(
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "sortField", required = false, defaultValue = "id") String sortField,
            @RequestParam(name = "sortDirection", required = false, defaultValue = "desc") String sortDirection,
            Model model
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        model.addAttribute("orderPage", orderService.findPaginatedByUserId(user.getId(), page, 5, sortField, sortDirection));
        model.addAttribute("balance", userService.findById(user.getId()).getBalance());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDir", sortDirection.equals("asc") ? "desc" : "asc");

        return "cabinet";
    }

    @PostMapping("/payForOrder")
    public String payForOrder(@RequestParam(name = "id") long id) {
        userService.payForOrder(orderService.findById(id));
        return "redirect:/cabinet";
    }

    @PostMapping("/topUpBalance")
    public String topUpBalance(@RequestParam(name = "amount") int amount) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.topUpBalance(user.getId(), amount);
        return "redirect:/cabinet";
    }
}
