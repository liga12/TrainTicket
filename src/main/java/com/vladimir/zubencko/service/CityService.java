package com.vladimir.zubencko.service;

import com.vladimir.zubencko.domain.City;

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
}
