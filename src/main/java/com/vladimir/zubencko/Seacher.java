package com.vladimir.zubencko;

import com.vladimir.zubencko.domain.Station;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Seacher {
    Integer transplantValue;
    Integer transplantTime;
    Station currentSourceStation ;
    Station currentDestinationStation;
    LocalDateTime sourceDate;
    LocalDateTime destinationDate;
    String message;

    public Seacher(Integer transplantValue, Integer transplantTime, Station currentSourceStation, Station currentDestinationStation, LocalDateTime sourceDate, LocalDateTime destinationDate, String message) {
        this.transplantValue = transplantValue;
        this.transplantTime = transplantTime;
        this.currentSourceStation = currentSourceStation;
        this.currentDestinationStation = currentDestinationStation;
        this.sourceDate = sourceDate.withSecond(0).withNano(0);
        this.destinationDate = destinationDate.withSecond(0).withNano(0);
        this.message = message;
    }
}
