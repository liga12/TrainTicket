package com.vladimir.zubencko;

import com.vladimir.zubencko.domain.City;
import com.vladimir.zubencko.domain.CityRepository;
import com.vladimir.zubencko.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TestApp {

    @Autowired
    CityService cityService;

    @Autowired
    CityRepository cityRepository;

    public void hh() throws CloneNotSupportedException, IOException, ClassNotFoundException {
        TicketService ticketService = new TicketService(cityService.searchByFullName("c2"), cityService.searchByFullName("c0"),  1);
        Way result = ticketService.test();
//        printWays(result);
    }


}
