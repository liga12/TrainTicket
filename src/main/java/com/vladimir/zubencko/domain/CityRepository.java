package com.vladimir.zubencko.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Integer> {
    @Query("select case when count (c) > 0  then true else false end from City c where c.name = :name")
    Boolean existsByName(@Param("name") String name);
    City findByName(String name);
    List<City> findByNameContaining(String name);
}
