package com.fredrik.bookit.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fredrik.bookit.model.Version;

@RestController
public class VersionController {

    @RequestMapping("/version")
    public Version version() {
        return new Version();
    }
}