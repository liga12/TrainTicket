package com.vladimir.zubencko.service;

import com.vladimir.zubencko.Seacher;
import com.vladimir.zubencko.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class StationServiceImpl implements StationService {
    @Autowired
    StationRepository stationRepository;

    @Autowired
    TrainWayService trainWayService;

    @Autowired
    NeighborStationService neighborStationService;

    @Autowired
    TrainService trainService;

    @Override
    @Transactional
    public void save(Station station) {
        stationRepository.saveAndFlush(station);
    }

    @Override
    public List<Station> getStation() {
        return stationRepository.findAll();
    }

    @Override
    public Boolean existsByName(String name) {
        return stationRepository.existsByName(name);
    }

    @Override
    public Station searchByFullName(String name) {
        return stationRepository.findByName(name);
    }

    @Override
    public List<Station> searchByBitName(String name) {
        return stationRepository.findByNameContaining(name);
    }

    @Override
    @Transactional
    public void delete(Station station) {
        stationRepository.delete(station);
    }

    @Override
    public List<Station> getAll() {
        return stationRepository.findAll();
    }

    @Override
    public List<Train> getStationLink(Station station, List<TrainWay> trainWays) {
        List<Train> trains = new ArrayList<>();
        for (TrainWay trainWay : trainWays) {
            Train train = trainWay.getTrain();
            trains.add(train);
        }
        return trains;
    }

    @Override
    public void deleteStationLink(Station station, List<TrainWay> trainWays, List<Train> trains) {
        List<NeighborStation> neighborCities = station.getNeighborStations();
        neighborStationService.delete(neighborCities);
        trainWayService.delete(trainWays);
        trainService.delete(trains);
        delete(station);
    }

    @Override
    public ModelAndView prepareDate(String station, List<String> trains, String url, String status) {
        ModelAndView modelAndView = new ModelAndView("confirm");
        if (station != null && trains != null) {
            if (!trains.isEmpty()) {
                List<Train> trains1 = new ArrayList<>();
                for (String trainName : trains) {
                    Train train = trainService.searchByFullName(trainName);
                    if (train != null) {
                        trains1.add(train);
                    }
                }
                modelAndView.addObject("station", station);
                modelAndView.addObject("trains", trains1);
                modelAndView.addObject("url", url);
                modelAndView.addObject("status", status);
            }
        }
        return modelAndView;
    }

    @Override
    public String validStationAndNeighbor(String stationName, List<String> neighborCities) {
        if (!"".equals(stationName)) {
            if (!existsByName(stationName)) {
                if (neighborCities != null) {
                    boolean result = neighborStationService.isEqual(neighborCities);
                    if (result) {
                        return "Neighbor station is equals";
                    }
                } else {
                    return "Neighbor station is empty";
                }
            } else {
                return "Station is exist";
            }
        } else {
            return "Station is empty";
        }
        return "OK";
    }

    @Override
    public String validEditStationAndNeighbor(String stationName, String stationOriginal, List<String> neighborCities) {
        if (!"".equals(stationName)) {
            boolean equalsStation;
            if (stationOriginal != null) {
                if (stationName.equals(stationOriginal)) {
                    equalsStation = false;
                } else {
                    equalsStation = existsByName(stationName);
                }
            } else {
                return "home";
            }
            if (!equalsStation) {
                if (neighborCities != null) {
                    String result = neighborStationService.checkNeighbors(neighborCities, stationName);
                    if (!result.equals("OK")) {
                        return result;
                    }
                } else {
                    return "Station is exist";
                }
            }
        } else {
            return "Station is empty";
        }
        return "OK";
    }

    @Override
    public Seacher validData(String valueTransplant, String waitingOnTransplant, String sourceStation,
                             String destinationStation, String sourceDateTime, String destinationDateTime) {
        Seacher seacher = new Seacher();
        if ("".equals(valueTransplant)){
            seacher.setMessage("Not correct value transplant");
            return seacher;
        }
        Integer transplantValue = validTransplant(valueTransplant, 0, 3);
        if (transplantValue == null) {
            seacher.setMessage("Not correct value transplant");
            return seacher;
        }
        if ("".equals(waitingOnTransplant)){
            seacher.setMessage("Not correct value transplant");
            return seacher;
        }
        Integer transplantTime = validTransplant(waitingOnTransplant, 0, 12);
        if (transplantTime == null) {
            seacher.setMessage("Not correct value transplant");
            return seacher;
        }
        if ("".equals(sourceStation)||"".equals(destinationStation)){
            seacher.setMessage("Not correct station name");
            return seacher;
        }
        if (sourceStation.equals(destinationStation)) {
            seacher.setMessage("Station is equals");
            return seacher;
        }
        Station currentSourceStation = validStation(sourceStation);
        Station currentDestinationStation = validStation(destinationStation);
        if (currentSourceStation == null || currentDestinationStation == null) {
            seacher.setMessage("Not correct station");
            return seacher;
        }

        if ("".equals(sourceDateTime)||"".equals(destinationDateTime)){
            seacher.setMessage("Not correct dateTime");
            return seacher;
        }
        LocalDateTime sourceDate = validDateTime(sourceDateTime);
        LocalDateTime destinationDate = validDateTime(destinationDateTime);
        if (sourceDate == null || destinationDate == null) {
            seacher.setMessage("Not correct date");
            return seacher;
        }
        int year =LocalDate.now().getYear();
        int time = LocalTime.now().withSecond(0).plusMinutes(5).toSecondOfDay();
        if (sourceDate.toLocalDate().getYear()<year||destinationDate.toLocalDate().getYear()<year){
            seacher.setMessage("Date later now date");
            return seacher;

//        }else {
//            if (sourceDate.toLocalDate().toEpochDay())
//            if (sourceDate.toLocalTime().toSecondOfDay()<time||destinationDate.toLocalTime().toSecondOfDay()<time){
//                seacher.setMessage("Time later now time");
//                return seacher;
//            }
        }
        if (sourceDate.toLocalDate().toEpochDay() > destinationDate.toLocalDate().toEpochDay()) {
            seacher.setMessage("Source station date > destination station date");
            return seacher;
        }
        if (sourceDate.toLocalDate().toEpochDay() == destinationDate.toLocalDate().toEpochDay()) {
            if (sourceDate.toLocalTime().toSecondOfDay() >= destinationDate.toLocalTime().minusMinutes(10).toSecondOfDay()) {
                seacher.setMessage("Source station time > or equals destination station time");
                return seacher;
            }
        }
        LocalDate localDate = LocalDate.now();
        if (sourceDate.toLocalDate().toEpochDay() < localDate.toEpochDay() ||
                destinationDate.toLocalDate().toEpochDay() < localDate.toEpochDay()) {
            seacher.setMessage("Date later now date");
            return seacher;
        }

        return new Seacher(transplantValue, transplantTime, currentSourceStation, currentDestinationStation,
                sourceDate, destinationDate, "OK");
    }

    @Override
    public Station validStation(String station) {
        return searchByFullName(station);
    }

    @Override
    public LocalDateTime validDateTime(String dataTime) {
        LocalDateTime dateTime1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        try {
            dateTime1 = LocalDateTime.parse(dataTime, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
        return dateTime1;
    }

    @Override
    public Integer validTransplant(String transplant, int start, int finish) {
        int transplantInt;
        try {
            transplantInt = Integer.valueOf(transplant);
        } catch (NumberFormatException e) {
            return null;
        }
        if (transplantInt > finish || transplantInt < start) {
            return null;
        }
        return transplantInt;
    }
}
