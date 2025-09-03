package com.aryan.store.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("name", "Aryan");
        return "index"; // the Thymeleaf searches "index.html" in resources-->templates folder
    }
}
