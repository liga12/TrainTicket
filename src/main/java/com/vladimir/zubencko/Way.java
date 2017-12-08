package com.vladimir.zubencko;

import com.vladimir.zubencko.domain.City;
import com.vladimir.zubencko.domain.Train;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Service
public class Way implements Serializable {

    private Train train;
    private City city;
    private int peresadka;
    private List<Way> ways;
    private boolean finishWay;
    private int coast;
    private List<City> visitedCities;
    private LocalDateTime otprTime;
    private LocalTime stopTime;

    public Way(Train train, City city, LocalDateTime otprTime, LocalTime stopTime, int peresadka) {
        this.train = train;
        this.city = city;
        this.otprTime = otprTime;
        this.stopTime = stopTime;
        this.peresadka = peresadka;
    }


}
