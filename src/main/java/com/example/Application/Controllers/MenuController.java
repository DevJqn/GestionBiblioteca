package com.example.Application.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {

    @GetMapping({"/", "/menu"})
    public String mostrarMenu() {
        return "menu"; // Thymeleaf buscará resources/templates/menu.html
    }
}

