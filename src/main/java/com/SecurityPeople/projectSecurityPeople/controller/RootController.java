package com.SecurityPeople.projectSecurityPeople.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/")
    public String home() {
        return "✅ API ProjectSecurityPeople corriendo correctamente en AWS Elastic Beanstalk";
    }
}
