package pl.oskarpolak.tradex.controllers;

import com.sun.tools.javac.util.DefinedBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.oskarpolak.tradex.models.CurrencyModel;
import pl.oskarpolak.tradex.models.services.CurrentService;

import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
public class ApiController {

    @Autowired
    CurrentService currentService;

    @GetMapping("/api/currency/{typed}")
    public ResponseEntity getCurrency(@PathVariable("typed") String typed){
         return ResponseEntity
                 .ok()
                 .body(currentService.getActualCurrencies()
                        .stream()
                        .filter(s -> s.contains(typed.toUpperCase()))
                        .collect(Collectors.toList()));
    }
}
