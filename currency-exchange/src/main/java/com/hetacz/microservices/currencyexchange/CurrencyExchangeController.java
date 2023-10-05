package com.hetacz.microservices.currencyexchange;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CurrencyExchangeController {

    @Autowired
    private Environment env;

    @Autowired
    private CurrencyExchangeRepository repository;

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyExchange retrieveExchangeValue(@PathVariable String from, @PathVariable String to) {
        log.info("The retrieveExchangeValue called with from {}, to {}.", from, to);
        CurrencyExchange exchange = repository
                .findByFromAndTo(from, to)
                .orElseThrow(() -> new RuntimeException("Unable to find data for " + from + " to " + to));
        exchange.setEnvironment(env.getProperty("local.server.port"));
        return exchange;
    }
}
