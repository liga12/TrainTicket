package com.vladimir.zubencko.service;

import com.vladimir.zubencko.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class NeighborStationServiceImpl implements NeighborStationService {
    @Autowired
    NeighborStationRepository neighborStationRepository;

    @Autowired
    StationService stationService;

    @Autowired
    TrainService trainService;

    @Override
    @Transactional
    public void save(NeighborStation neighborStation) {
        neighborStationRepository.saveAndFlush(neighborStation);
    }

    @Override
    @Transactional
    public void delete(NeighborStation neighborStation) {
        neighborStationRepository.delete(neighborStation);
    }

    @Override
    @Transactional
    public void delete(List<NeighborStation> neighborCities) {
        neighborStationRepository.delete(neighborCities);
    }

    @Override
    public void saveNeighbors(Station station, List<String> neighborCities) {
        for (String neighborStation : neighborCities) {
            if (!"None".equals(neighborStation)) {
                Station neighbor = stationService.searchByFullName(neighborStation);
                NeighborStation neighborStation1 = new NeighborStation(station, neighbor);
                save(neighborStation1);
            }
        }
    }

    @Override
    public void saveEditNeighbors(Station station, List<String> neighborCities) {
        for (String currentNeighborStation : neighborCities) {
            if (!"None".equals(currentNeighborStation)) {
                Station neighbor = stationService.searchByFullName(currentNeighborStation);
                List<NeighborStation> cities = station.getNeighborStations();
                boolean needUpdate = true;
                if (cities != null && !cities.isEmpty()) {
                    for (NeighborStation neighborStation : cities) {
                        if (neighbor.equals(neighborStation.getNeighborStation())) {
                            needUpdate = false;
                            break;
                        }
                    }
                }
                if (needUpdate) {
                    NeighborStation neighborStation = new NeighborStation(station, neighbor);
                    save(neighborStation);
                }
            }
        }
    }

    @Override
    public List<Train> checkEditStationLink(List<NeighborStation> deletedNeighbors, Station station) {
        List<Train> deletedTrains = new ArrayList<>();
        if (deletedNeighbors != null) {
            List<TrainWay> trainWays = station.getTrainWays();
            if (trainWays != null) {
                for (NeighborStation deletedNeighbor : deletedNeighbors) {
                    Station neighbor = deletedNeighbor.getNeighborStation();
                    List<Train> stationTrains = new ArrayList<>();
                    for (TrainWay trainWay : trainWays) {
                        stationTrains.add(trainWay.getTrain());
                    }
                    for (Train stationTrain : stationTrains) {
                        List<TrainWay> currentTrainTrainWays = stationTrain.getTrainWays();
                        for (int i = 0; i < currentTrainTrainWays.size(); i++) {
                            if (i != currentTrainTrainWays.size() - 1) {
                                Station currentStationWay = currentTrainTrainWays.get(i).getStation();
                                Station nextStationWay = currentTrainTrainWays.get(i + 1).getStation();
                                if (currentStationWay.equals(station) && nextStationWay.equals(neighbor)) {
                                    Train train = currentTrainTrainWays.get(i).getTrain();
                                    deletedTrains.add(train);
                                }
                            }

                        }
                    }
                }
            }

        }
        return deletedTrains;
    }

    @Override
    public List<NeighborStation> deleteEditNeighbor(Station station, List<String> neighborCities) {
        List<NeighborStation> deletedNeighbors = new ArrayList<>();
        List<NeighborStation> neighbors = station.getNeighborStations();
        if (neighbors != null && !neighbors.isEmpty()) {
            for (NeighborStation neighborStation : neighbors) {
                Station neighbor = neighborStation.getNeighborStation();
                boolean needDelete = true;
                for (String nameStation : neighborCities) {
                    if (!nameStation.equals("None")) {
                        Station currentStation = stationService.searchByFullName(nameStation);
                        if (neighbor.equals(currentStation)) {
                            needDelete = false;
                            break;
                        }
                    }
                }
                if (needDelete) {
                    deletedNeighbors.add(neighborStation);
                }
            }
        }
        return deletedNeighbors;
    }

    @Override
    public boolean isEqual(List<String> neighborCities) {
        for (int i = 0; i < neighborCities.size(); i++) {
            if (neighborCities.get(i).equals("None")) {
                continue;
            } else {
                for (int j = 0; j < neighborCities.size(); j++) {
                    if (j == i) {
                        continue;
                    } else {
                        if (neighborCities.get(i).equals(neighborCities.get(j))) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String checkNeighbors(List<String> neighborCities, String stationName) {
        for (int i = 0; i < neighborCities.size(); i++) {
            if (neighborCities.get(i).equals("None")) {
                continue;
            } else {
                for (int j = 0; j < neighborCities.size(); j++) {
                    if (stationName.equals(neighborCities.get(i))) {
                        return "Neighbor station is equals neighborStation";
                    }
                    if (j == i) {
                        continue;
                    } else {
                        if (neighborCities.get(i).equals(neighborCities.get(j))) {
                            return "Neighbor station is equals";
                        }
                    }
                }
            }
        }
        return "OK";
    }

    @Override
    public void deleteOldNeighbor(List<NeighborStation> deletedNeighbors, List<Train> deletedTrains) {
        delete(deletedNeighbors);
        trainService.delete(deletedTrains);

    }

}
