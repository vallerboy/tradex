package pl.oskarpolak.tradex.controllers;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import pl.oskarpolak.tradex.models.CurrencyModel;
import pl.oskarpolak.tradex.models.HttpErrorHandler;

import java.util.Arrays;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
public class MainController {

    @GetMapping("/{currencyOne}/{currencyTwo}")
    public String index(Model model,
                        @PathVariable("currencyOne") String currencyOne,
                        @PathVariable("currencyTwo") String currencyTwo ) {
        ResponseEntity<CurrencyModel> currencyModel;
      try {
           currencyModel = getRestTemplate().getForEntity("http://api.nbp.pl/api/exchangerates/rates/a/" + currencyOne + "/last/10/?format=json", CurrencyModel.class);
      }catch (IllegalStateException e){
          return "redirect:/";
      }


        model.addAttribute("labels", currencyModel.getBody().getRates().stream().map(s -> s.getBidData()).collect(Collectors.toList()));


        model.addAttribute("currencyNameOne", currencyModel.getBody().getCurrencyName());
        model.addAttribute("dataOne", currencyModel.getBody().getRates().stream().map(s -> s.getBid()).collect(Collectors.toList()));


        ResponseEntity<CurrencyModel> currencyModel2;
        try {
            currencyModel2 = getRestTemplate().getForEntity("http://api.nbp.pl/api/exchangerates/rates/a/" + currencyTwo + "/last/10/?format=json", CurrencyModel.class);
        }catch (IllegalStateException e){
            return "redirect:/";
        }

        model.addAttribute("currencyNameTwo", currencyModel2.getBody().getCurrencyName());
        model.addAttribute("dataTwo", currencyModel2.getBody().getRates().stream().map(s -> s.getBid()).collect(Collectors.toList()));


        return "index";
    }

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplateBuilder().errorHandler(new HttpErrorHandler()).build();
    }

}
