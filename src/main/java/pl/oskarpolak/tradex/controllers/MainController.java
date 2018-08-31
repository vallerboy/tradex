package pl.oskarpolak.tradex.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.NoHandlerFoundException;
import pl.oskarpolak.tradex.models.CurrencyModel;
import pl.oskarpolak.tradex.models.HttpErrorHandler;
import pl.oskarpolak.tradex.models.services.SmsService;

import java.util.Arrays;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
public class MainController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @Autowired
    SmsService smsService;


    @GetMapping("/{currencyOne}/{currencyTwo}")
    public String index(Model model,
                        @PathVariable("currencyOne") String currencyOne,
                        @PathVariable("currencyTwo") String currencyTwo ) {
        ResponseEntity<CurrencyModel> currencyModel;
      try {
           currencyModel = getRestTemplate().getForEntity("http://api.nbp.pl/api/exchangerates/rates/a/" + currencyOne + "/last/10/?format=json", CurrencyModel.class);
      }catch (IllegalStateException e){
          LOGGER.error("Can not get currency: " + currencyOne);
          return "redirect:/";
      }

//      if(currencyModel.getBody().getRates().stream().anyMatch(s -> s.getBid() > 3.60)){
//
//      }

        model.addAttribute("labels", currencyModel.getBody().getRates().stream().map(s -> s.getBidData()).collect(Collectors.toList()));


        model.addAttribute("currencyNameOne", currencyModel.getBody().getCurrencyName());
        model.addAttribute("dataOne", currencyModel.getBody().getRates().stream().map(s -> s.getBid()).collect(Collectors.toList()));


        ResponseEntity<CurrencyModel> currencyModel2;
        try {
            currencyModel2 = getRestTemplate().getForEntity("http://api.nbp.pl/api/exchangerates/rates/a/" + currencyTwo + "/last/10/?format=json", CurrencyModel.class);
        }catch (IllegalStateException e){
            LOGGER.error("Can not get currency: " + currencyOne);
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


    @GetMapping("/*")
    @ResponseBody
    public String error404() {
        return "blad nie znaleziono!";
    }


    @GetMapping("/sms/{number}/{text}")
    @ResponseBody
    public String sendSms(@PathVariable("number") String number,
                          @PathVariable("text") String text){
        smsService.sendSms(number, text);
        return "niby poszło";
    }
}
