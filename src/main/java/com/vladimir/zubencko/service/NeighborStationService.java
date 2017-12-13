package com.vladimir.zubencko.service;

import com.vladimir.zubencko.domain.Station;
import com.vladimir.zubencko.domain.NeighborStation;
import com.vladimir.zubencko.domain.Train;

import java.util.List;

public interface NeighborStationService {
    void save(NeighborStation neighborStation);

    void saveEditNeighbors(Station station, List<String> neighborCities);

    void saveNeighbors(Station station, List<String> neighborCities);

    boolean isEqual(List<String> neighborCities);

    String checkNeighbors(List<String> neighborCities, String stationName);

    void delete(NeighborStation neighborStation);

    void delete(List<NeighborStation> neighborCities);

    List<NeighborStation> deleteEditNeighbor(Station station, List<String> neighborCities);

    List<Train> checkEditStationLink(List<NeighborStation> deletedNeighbors, Station station);

    void deleteOldNeighbor(List<NeighborStation> deletedNeighbors, List<Train> deletedTrains);
}
