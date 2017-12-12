package com.vladimir.zubencko.service;

import com.vladimir.zubencko.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Service
public class CityServiceImpl implements CityService {
    @Autowired
    CityRepository cityRepository;

    @Autowired
    TrainWayService trainWayService;

    @Autowired
    NeighborCityService neighborCityService;

    @Autowired
    TrainService trainService;

    @Override
    @Transactional
    public void save(City city) {
        cityRepository.saveAndFlush(city);
    }

    @Override
    public List<City> getCities() {
        return cityRepository.findAll();
    }

    @Override
    public Boolean existsByName(String name) {
        return cityRepository.existsByName(name);
    }

    @Override
    public City searchByFullName(String name) {
        return cityRepository.findByName(name);
    }

    @Override
    public List<City> searchByBitName(String name) {
        return cityRepository.findByNameContaining(name);
    }

    @Override
    @Transactional
    public void delete(City city) {
        cityRepository.delete(city);
    }

    @Override
    public List<City> getAll() {
        return cityRepository.findAll();
    }

    @Override
    public List<Train> getCityLink(City city, List<TrainWay> trainWays) {
        List<Train> trains = new ArrayList<>();
        for (TrainWay trainWay : trainWays) {
            Train train = trainWay.getTrain();
            trains.add(train);
        }
        return trains;
    }

    @Override
    public void deleteCityLink(City city, List<TrainWay> trainWays, List<Train> trains) {
        List<NeighborCity> neighborCities = city.getNeighborCities();
        neighborCityService.delete(neighborCities);
        trainWayService.delete(trainWays);
        trainService.delete(trains);
        delete(city);
    }

    @Override
    public ModelAndView prepareDate(String city, List<String> trains, String url, String status) {
        ModelAndView modelAndView = new ModelAndView("confirm");
        if (city != null && trains != null) {
            if (!trains.isEmpty()) {
                List<Train> trains1 = new ArrayList<>();
                for (String trainName : trains) {
                    Train train = trainService.searchByFullName(trainName);
                    if (train != null) {
                        trains1.add(train);
                    }
                }
                modelAndView.addObject("city", city);
                modelAndView.addObject("trains", trains1);
                modelAndView.addObject("url", url);
                modelAndView.addObject("status", status);
            }
        }
        return modelAndView;
    }

    @Override
    public String validCityAndNeighbor(String cityName, List<String> neighborCities) {
        if (!"".equals(cityName)) {
            if (!existsByName(cityName)) {
                if (neighborCities != null) {
                    boolean result = neighborCityService.isEqual(neighborCities);
                    if (result) {
                        return "Neighbor cities is equals";
                    }
                } else {
                    return "Neighbor cities is empty";
                }
            } else {
                return "City is exist";
            }
        } else {
            return "City is empty";
        }
        return "OK";
    }

    @Override
    public String validEditCityAndNeighbor(String cityName, String cityOriginal, List<String> neighborCities) {
        if (!"".equals(cityName)) {
            boolean equalsCity;
            if (cityOriginal != null) {
                if (cityName.equals(cityOriginal)) {
                    equalsCity = false;
                } else {
                    equalsCity = existsByName(cityName);
                }
            } else {
                return "home";
            }
            if (!equalsCity) {
                if (neighborCities != null) {
                    String result = neighborCityService.checkNeighbors(neighborCities, cityName);
                    if (!result.equals("OK")) {
                        return result;
                    }
                } else {
                    return "City is exist";
                }
            }
        } else {
            return "City is empty";
        }
        return "OK";
    }
}
