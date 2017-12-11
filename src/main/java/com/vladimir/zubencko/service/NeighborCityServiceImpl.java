package com.vladimir.zubencko.service;

import com.vladimir.zubencko.domain.City;
import com.vladimir.zubencko.domain.NeighborCity;
import com.vladimir.zubencko.domain.NeighborRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NeighborCityServiceImpl implements NeighborCityService {
    @Autowired
    NeighborRepository neighborRepository;

    @Autowired
    CityService cityService;

    @Override
    @Transactional
    public void save(NeighborCity neighborCity) {
        neighborRepository.saveAndFlush(neighborCity);
    }
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void delete(NeighborCity neighborCity){
        neighborRepository.delete(neighborCity);
    }

    @Override
    public void saveNeighbors(City city, List<String> neighborCities){
        for (String neighborCity : neighborCities) {
            if (!neighborCity.equals("None")){
                City neighbor = cityService.searchByFullName(neighborCity);
                NeighborCity neighborCity1 = new NeighborCity(city, neighbor);
                save(neighborCity1);
            }
        }
    }

    @Override
    public void saveEditNeighbors(City city, List<String> neighborCities){
        for (String currentNeighborCity : neighborCities) {
            if (!currentNeighborCity.equals("None")) {
                City neighbor = cityService.searchByFullName(currentNeighborCity);
                List<NeighborCity> cities = city.getNeighborCities();
                boolean update = true;
                if (cities!=null&&!cities.isEmpty()){
                    for (NeighborCity neighborCity : cities) {
                        if (neighbor.equals(neighborCity.getNeighborCity())){
                            update = false;
                            break;
                        }
                    }
                }

                if (update){
                    NeighborCity neighborCity = new NeighborCity(city, neighbor);
                    save(neighborCity);
                }

            }
        }
        List<NeighborCity> neighbors = city.getNeighborCities();
        if (neighbors!=null&&!neighbors.isEmpty()) {
            for (NeighborCity neighborCity : neighbors) {
                City neighbor = neighborCity.getNeighborCity();
                boolean delete =true;
                for (String nameCity : neighborCities) {
                    if (!nameCity.equals("None")){
                        City currentCity = cityService.searchByFullName(nameCity);
                        if (neighbor.equals(currentCity)){
                            delete = false;
                            break;
                        }
                    }
                }
                if (delete){
                    delete(neighborCity);
                }
            }
        }

    }

    @Override
    public  boolean isEqual(List<String> neighborCities){
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
    public String checkNeighbors(List<String> neighborCities, String cityName){
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

}
