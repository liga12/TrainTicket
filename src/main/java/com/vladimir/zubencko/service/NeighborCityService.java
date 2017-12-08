package com.vladimir.zubencko.service;

import com.vladimir.zubencko.domain.City;
import com.vladimir.zubencko.domain.NeighborCity;

import java.util.List;

public interface NeighborCityService {
    void save(NeighborCity neighborCity);
    void saveEditNeighbors(City city, List<String> neighborCities);
    void saveNeighbors(City city, List<String> neighborCities);
    boolean isEqual(List<String> neighborCities);
}
