package pl.oskarpolak.tradex.controllers;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import pl.oskarpolak.tradex.models.CurrencyModel;

import java.util.Arrays;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
public class MainController {

    @GetMapping("/{currencyOne}/{currencyTwo}")
    public String index(Model model,
                        @PathVariable("currencyOne") String currencyOne,
                        @PathVariable("currencyTwo") String currencyTwo ) {
        CurrencyModel currencyModel = getRestTemplate().getForObject("http://api.nbp.pl/api/exchangerates/rates/a/" + currencyOne + "/last/10/?format=json", CurrencyModel.class);

        model.addAttribute("labels", currencyModel.getRates().stream().map(s -> s.getBidData()).collect(Collectors.toList()));


        model.addAttribute("currencyNameOne", currencyModel.getCurrencyName());
        model.addAttribute("dataOne", currencyModel.getRates().stream().map(s -> s.getBid()).collect(Collectors.toList()));


        CurrencyModel currencyModel2 = getRestTemplate().getForObject("http://api.nbp.pl/api/exchangerates/rates/a/" + currencyTwo + "/last/10/?format=json", CurrencyModel.class);
        model.addAttribute("currencyNameTwo", currencyModel2.getCurrencyName());
        model.addAttribute("dataTwo", currencyModel2.getRates().stream().map(s -> s.getBid()).collect(Collectors.toList()));


        return "index";
    }

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

}
