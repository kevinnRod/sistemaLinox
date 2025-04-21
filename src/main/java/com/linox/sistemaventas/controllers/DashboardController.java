package com.linox.sistemaventas.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "hola"; // Esto busca una plantilla "dashboard.html" en src/main/resources/templates/
    }
}
