package com.vladimir.zubencko.service;

import com.vladimir.zubencko.domain.City;
import com.vladimir.zubencko.domain.CityRepository;
import com.vladimir.zubencko.domain.NeighborCity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {
    @Autowired
    CityRepository cityRepository;

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
    public void delete(City city) {
     cityRepository.delete(city);
    }

    @Override
    public List<City> getAll() {
        return cityRepository.findAll();
    }


}
