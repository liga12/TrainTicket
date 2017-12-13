package com.vladimir.zubencko;

import com.vladimir.zubencko.domain.Station;
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
    private Station station;
    private int transplant;
    private List<Way> ways;
    private boolean finishWay;
    private int cost;
    private List<Station> visitedCities;
    private LocalDateTime departureTime;
    private LocalTime stoppingTime;

    public Way(Train train, Station station, LocalDateTime departureTime, LocalTime stoppingTime, int transplant) {
        this.train = train;
        this.station = station;
        this.departureTime = departureTime;
        this.stoppingTime = stoppingTime;
        this.transplant = transplant;
    }


}
