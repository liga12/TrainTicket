package com.vladimir.zubencko.service;

import com.vladimir.zubencko.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void deleteCityLink(City city, List<TrainWay> trainWays,List<Train> trains) {
        List<NeighborCity> neighborCities = city.getNeighborCities();
        neighborCityService.delete(neighborCities);
        trainWayService.delete(trainWays);
        trainService.delete(trains);
        delete(city);
    }


}
