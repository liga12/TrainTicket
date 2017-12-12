package com.vladimir.zubencko.service;

import com.vladimir.zubencko.domain.City;
import com.vladimir.zubencko.domain.NeighborCity;
import com.vladimir.zubencko.domain.Train;

import java.util.List;

public interface NeighborCityService {
    void save(NeighborCity neighborCity);

    void saveEditNeighbors(City city, List<String> neighborCities);

    void saveNeighbors(City city, List<String> neighborCities);

    boolean isEqual(List<String> neighborCities);

    String checkNeighbors(List<String> neighborCities, String cityName);

    void delete(NeighborCity neighborCity);

    void delete(List<NeighborCity> neighborCities);

    List<NeighborCity> deleteEditNeighbor(City city, List<String> neighborCities);

    List<Train> checkEditCityLink(List<NeighborCity> deletedNeighbors, City city);

    void deleteOldNeighbor(List<NeighborCity> deletedNeighbors, List<Train> deletedTrains);
}
