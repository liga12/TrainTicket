package com.vladimir.zubencko.service;

import com.vladimir.zubencko.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

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
}
