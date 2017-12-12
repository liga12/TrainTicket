package com.vladimir.zubencko.service;

import com.vladimir.zubencko.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class NeighborCityServiceImpl implements NeighborCityService {
    @Autowired
    NeighborRepository neighborRepository;

    @Autowired
    CityService cityService;

    @Autowired
    TrainService trainService;

    @Override
    @Transactional
    public void save(NeighborCity neighborCity) {
        neighborRepository.saveAndFlush(neighborCity);
    }

    @Override
    @Transactional
    public void delete(NeighborCity neighborCity) {
        neighborRepository.delete(neighborCity);
    }

    @Override
    @Transactional
    public void delete(List<NeighborCity> neighborCities) {
        neighborRepository.delete(neighborCities);
    }

    @Override
    public void saveNeighbors(City city, List<String> neighborCities) {
        for (String neighborCity : neighborCities) {
            if (!"None".equals(neighborCity)) {
                City neighbor = cityService.searchByFullName(neighborCity);
                NeighborCity neighborCity1 = new NeighborCity(city, neighbor);
                save(neighborCity1);
            }
        }
    }

    @Override
    public void saveEditNeighbors(City city, List<String> neighborCities) {
        for (String currentNeighborCity : neighborCities) {
            if (!"None".equals(currentNeighborCity)) {
                City neighbor = cityService.searchByFullName(currentNeighborCity);
                List<NeighborCity> cities = city.getNeighborCities();
                boolean needUpdate = true;
                if (cities != null && !cities.isEmpty()) {
                    for (NeighborCity neighborCity : cities) {
                        if (neighbor.equals(neighborCity.getNeighborCity())) {
                            needUpdate = false;
                            break;
                        }
                    }
                }
                if (needUpdate) {
                    NeighborCity neighborCity = new NeighborCity(city, neighbor);
                    save(neighborCity);
                }
            }
        }
    }

    @Override
    public List<Train> checkEditCityLink(List<NeighborCity> deletedNeighbors, City city) {
        List<Train> deletedTrains = new ArrayList<>();
        if (deletedNeighbors != null) {
            List<TrainWay> trainWays = city.getTrainWays();
            if (trainWays != null) {
                for (NeighborCity deletedNeighbor : deletedNeighbors) {
                    City neighbor = deletedNeighbor.getNeighborCity();
                    List<Train> cityTrains = new ArrayList<>();
                    for (TrainWay trainWay : trainWays) {
                        cityTrains.add(trainWay.getTrain());
                    }
                    for (Train cityTrain : cityTrains) {
                        List<TrainWay> currentTrainTrainWays = cityTrain.getTrainWays();
                        for (int i = 0; i < currentTrainTrainWays.size(); i++) {
                            if (i != currentTrainTrainWays.size() - 1) {
                                City currentCityWay = currentTrainTrainWays.get(i).getCity();
                                City nextCityWay = currentTrainTrainWays.get(i + 1).getCity();
                                if (currentCityWay.equals(city) && nextCityWay.equals(neighbor)) {
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
    public List<NeighborCity> deleteEditNeighbor(City city, List<String> neighborCities) {
        List<NeighborCity> deletedNeighbors = new ArrayList<>();
        List<NeighborCity> neighbors = city.getNeighborCities();
        if (neighbors != null && !neighbors.isEmpty()) {
            for (NeighborCity neighborCity : neighbors) {
                City neighbor = neighborCity.getNeighborCity();
                boolean needDelete = true;
                for (String nameCity : neighborCities) {
                    if (!nameCity.equals("None")) {
                        City currentCity = cityService.searchByFullName(nameCity);
                        if (neighbor.equals(currentCity)) {
                            needDelete = false;
                            break;
                        }
                    }
                }
                if (needDelete) {
                    deletedNeighbors.add(neighborCity);
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
    public String checkNeighbors(List<String> neighborCities, String cityName) {
        for (int i = 0; i < neighborCities.size(); i++) {
            if (neighborCities.get(i).equals("None")) {
                continue;
            } else {
                for (int j = 0; j < neighborCities.size(); j++) {
                    if (cityName.equals(neighborCities.get(i))) {
                        return "Neighbor city is equals city";
                    }
                    if (j == i) {
                        continue;
                    } else {
                        if (neighborCities.get(i).equals(neighborCities.get(j))) {
                            return "Neighbor cities is equals";
                        }
                    }
                }
            }
        }
        return "OK";
    }

    @Override
    public void deleteOldNeighbor(List<NeighborCity> deletedNeighbors, List<Train> deletedTrains) {
        delete(deletedNeighbors);
        trainService.delete(deletedTrains);

    }

}
