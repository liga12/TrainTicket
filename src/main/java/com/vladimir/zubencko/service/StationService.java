package com.vladimir.zubencko.service;

import com.vladimir.zubencko.Seacher;
import com.vladimir.zubencko.domain.Station;
import com.vladimir.zubencko.domain.Train;
import com.vladimir.zubencko.domain.TrainWay;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public interface StationService {
    void save(Station station);

    List<Station> getStation();

    Boolean existsByName(String name);

    Station searchByFullName(String name);

    List<Station> searchByBitName(String name);

    void delete(Station station);

    List<Station> getAll();

    List<Train> getStationLink(Station station, List<TrainWay> trainWays);

    void deleteStationLink(Station station, List<TrainWay> trainWays, List<Train> trains);

    ModelAndView prepareDate(String station, List<String> trains, String URL, String status);

    String validStationAndNeighbor(String stationName, List<String> neighborCities);

    String validEditStationAndNeighbor(String stationName, String stationOriginal, List<String> neighborCities);

    Station validStation(String station);

    Integer validTransplant(String transplant, int start, int finish);

    LocalDateTime validDateTime(String dataTime);

    Seacher validData(String valueTransplant, String waitingOnTransplant, String sourceStation,
                      String destinationStation, String sourceDateTime, String destinationDateTime);

}
