package com.vladimir.zubencko;

import com.vladimir.zubencko.domain.City;
import com.vladimir.zubencko.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class TrainTicket extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(TrainTicket.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TrainTicket.class);
    }

}
