package com.hetacz.microservices.limitsservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LimitController {

    @Autowired
    private LimitConfiguration configuration;

    @GetMapping("/limits")
    public Limit retrieveLimits() {
        return new Limit(configuration.getMinimum(), configuration.getMaximum());
        // return new Limit(1, 1000);
    }
}
