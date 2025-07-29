package com.example.AuthService.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Responsible for html pages.
 */
@Controller
@RequestMapping("/form")
public class FormController {

    /**
     * Provides custom login page.
     *
     * @return
     */
    @GetMapping("/")
    public String home() {
        return "login";
    }

}
