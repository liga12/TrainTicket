package com.vladimir.zubencko;

import com.vladimir.zubencko.domain.City;
import com.vladimir.zubencko.domain.CityRepository;
import com.vladimir.zubencko.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class TestApp {

    public static void main(String[] args) {
        System.out.println(LocalTime.now().getMinute());
    }


}
