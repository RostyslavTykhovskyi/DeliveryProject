package com.mycompany.delivery.controller;

import com.mycompany.delivery.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    private final RouteService routeService;

    @Autowired
    public MainController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping("/")
    public String getMainPage(
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "sortField", required = false, defaultValue = "id") String sortField,
            @RequestParam(name = "sortDirection", required = false, defaultValue = "desc") String sortDirection,
            Model model
    ) {
        model.addAttribute("routePage", routeService.findPaginated(page, 5, sortField, sortDirection));
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDir", sortDirection.equals("asc") ? "desc" : "asc");

        return "main";
    }

    @RequestMapping("/login")
    public String getLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        model.addAttribute("error", error != null);
        return "login";
    }
}
