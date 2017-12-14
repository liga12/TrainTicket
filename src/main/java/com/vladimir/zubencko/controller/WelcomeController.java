package com.vladimir.zubencko.controller;

import com.vladimir.zubencko.Seacher;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    Way currentWay;


    @GetMapping("/")
    public String home1() throws IOException, ClassNotFoundException {
//        userService.registrationUser("11","11");
//        List<Station> stations = cityRepository.findAll();
//        LocalDateTime dateTime = LocalDateTime.now().withSecond(0);
//        for (Station station : stations) {
//            for (Station station1 : stations) {
//                System.out.println("st = " + station.getName() + " fin = " + station1.getName());
//                TicketService ticketService = new TicketService();
////                TicketService ticketService = new TicketService(cityService.searchByFullName("c0"), cityService.searchByFullName("c1"),  1);
//                Way result = ticketService.getWay(station, station1, 1, dateTime, 12);
//                ticketService.printWays(result);
//            }
//        }

        return "redirect:/home";
    }


    @GetMapping("/home")
    public ModelAndView home(@RequestParam(value = "error", required = false) String error,
                             @RequestParam(value = "way", required = false) String way) {
        ModelAndView modelAndView = new ModelAndView("home");
        List<Station> stations = stationService.getAll();
        modelAndView.addObject("stations", stations);
        if ("".equals(error)){
            modelAndView.addObject("error", error);
        }
        if ("".equals(way)){
            modelAndView.addObject("way", currentWay);
        }


        return modelAndView;
    }

    @GetMapping("/home/searchTrain")
    public ModelAndView searchTrain(@RequestParam(value = "valueTransplant") String valueTransplant,
                              @RequestParam(value = "waiting") String waitingOnTransplant,
                              @RequestParam(value = "sourceStation") String sourceStation,
                              @RequestParam(value = "sourceDateTime") String sourceDateTime,
                              @RequestParam(value = "destinationStation") String destinationStation,
                              @RequestParam(value = "destinationDateTime") String destinationDateTime
                              ) throws IOException, ClassNotFoundException {
        ModelAndView modelAndView = new ModelAndView("home");
        Seacher seacher = stationService.validData(valueTransplant, waitingOnTransplant, sourceStation,
                destinationStation, sourceDateTime, destinationDateTime);
        if (!seacher.getMessage().equals("OK")) {
            modelAndView.addObject("error", seacher.getMessage());
        } else {

            TicketService ticketService = new TicketService();
            Way way = ticketService.getWay(seacher.getCurrentSourceStation(), seacher.getCurrentDestinationStation(),
                    seacher.getTransplantValue(), seacher.getSourceDate().withSecond(0),
                    seacher.getDestinationDate().withSecond(0), seacher.getTransplantTime());
            ticketService.printWays(way);
            if (way != null&&!way.getWays().isEmpty()) {
                modelAndView.addObject("way", way);
            }else {
                modelAndView.addObject("way", null);
            }

        }
        List<Station> stations = stationService.getAll();
        modelAndView.addObject("stations", stations);
        modelAndView.addObject("destinationDateTime", destinationDateTime);
        modelAndView.addObject("sourceDateTime", sourceDateTime);
        return modelAndView;
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

