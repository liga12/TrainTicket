package com.vladimir.zubencko.controller;

import com.vladimir.zubencko.TicketService;
import com.vladimir.zubencko.Way;
import com.vladimir.zubencko.domain.Station;
import com.vladimir.zubencko.domain.StationRepository;
import com.vladimir.zubencko.service.StationService;
import com.vladimir.zubencko.service.UserService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@Log4j
public class WelcomeController {
    @Autowired
    UserService userService;

    @Autowired
    StationService stationService;

    @Autowired
    StationRepository cityRepository;


    @GetMapping("/")
    public String home1() throws  IOException, ClassNotFoundException {
//        userService.registrationUser("11","11");
        List<Station> cities = cityRepository.findAll();
        LocalDateTime dateTime = LocalDateTime.now().withSecond(0);
        for (Station station : cities) {
            for (Station station1 : cities) {
                System.out.println("st = " + station.getName() + " fin = " + station1.getName());
                TicketService ticketService = new TicketService();
//                TicketService ticketService = new TicketService(cityService.searchByFullName("c0"), cityService.searchByFullName("c1"),  1);
                Way result = ticketService.getWay(station, station1, 1, dateTime, 12);
                ticketService.printWays(result);
            }
        }

        return "redirect:/home";
    }

//    @GetMapping("/s")
//    public  s() throws CloneNotSupportedException, IOException, ClassNotFoundException {
//        ModelAndView  modelAndView = new ModelAndView("home");
//        TicketService ticketService = new TicketService(cityService.searchByFullName("c2"), cityService.searchByFullName("c0"),  1);
//        Way result = ticketService.getWay();
//        ticketService.printWays(result);
//        modelAndView.addObject("result", result);
//        return modelAndView;
//    }

    @GetMapping("/home")
    public String home(@RequestParam(value = "error", required = false) String error) {
        return "home";
    }

    @GetMapping("/login")
    public ModelAndView admin(@RequestParam(value = "error", required = false) String error) {

        ModelAndView model = new ModelAndView("login");
        if (userService.isAuthenticate()) {
            log.debug("Redirect from URL = /admin/home");
            return new ModelAndView("redirect:/admin/home");
        }
        if (error != null) {
            model.addObject("error", "Invalid login or password");
        }
        return model;
    }

    @GetMapping("/logout")
    public String lo() {
        return "logout";
    }


}

