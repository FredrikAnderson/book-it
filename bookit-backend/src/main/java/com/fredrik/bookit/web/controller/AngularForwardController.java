package com.fredrik.bookit.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AngularForwardController {

//    @RequestMapping(value = "/**/{[path:[^\\.]*}")
    @RequestMapping({ "/items", "/users", "/user/**", "/projects", "/project/**", "/projects-gantt", "/logout", "/home" })
    public String redirect() {
        // Forward to home page so that route is preserved.
        return "forward:/index.html";
    }
} 