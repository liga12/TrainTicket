package com.vladimir.zubencko.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StationRepository extends JpaRepository<Station, Integer> {
    @Query("select case when count (c) > 0  then true else false end from Station c where c.name = :name")
    Boolean existsByName(@Param("name") String name);
    Station findByName(String name);
    List<Station> findByNameContaining(String name);
}
