package com.vladimir.zubencko.service;

import com.vladimir.zubencko.domain.City;
import com.vladimir.zubencko.domain.NeighborCity;
import com.vladimir.zubencko.domain.Train;
import com.vladimir.zubencko.domain.TrainWay;

import java.io.Serializable;
import java.util.List;

public interface CityService {
    void save(City city);

    List<City> getCities();

    Boolean existsByName(String name);

    City searchByFullName(String name);

    List<City> searchByBitName(String name);

    void delete(City city);

    List<City> getAll();

    List<Train> getCityLink(City city, List<TrainWay> trainWays);

    void deleteCityLink(City city, List<TrainWay> trainWays, List<Train> trains);
}
