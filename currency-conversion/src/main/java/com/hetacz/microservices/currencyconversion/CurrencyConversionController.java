package com.hetacz.microservices.currencyconversion;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
public class CurrencyConversionController {

    @Autowired
    private CurrencyExchangeProxy proxy;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversion(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
        log.info("The calculateCurrencyConversion called with from {}, to {}, quantity {}.", from, to, quantity);
        Map<String, String> uriVariables = Map.of("from", from, "to", to);
        ResponseEntity<CurrencyConversion> response = restTemplate
//               .getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class, uriVariables);
                .getForEntity("http://currency-exchange:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class, uriVariables); // docker
        CurrencyConversion conversion = Objects.requireNonNull(response.getBody());
        BigDecimal conversionMultiple = conversion.getConversionMultiple();
        String environment = conversion.getEnvironment() + " rest template";
        return new CurrencyConversion(10001L, from, to, quantity, conversionMultiple, quantity.multiply(conversionMultiple), environment);
    }

    @GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversionFeign(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
        log.info("The calculateCurrencyConversion called with from {}, to {}, quantity {}.", from, to, quantity);
        CurrencyConversion conversion = proxy.retrieveExchangeValue(from, to);
        BigDecimal conversionMultiple = conversion.getConversionMultiple();
        String environment = conversion.getEnvironment() + " feign";
        return new CurrencyConversion(10002L, from, to, quantity, conversionMultiple, quantity.multiply(conversionMultiple), environment);
    }
}
