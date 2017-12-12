package com.vladimir.zubencko.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainWayRepository extends JpaRepository<TrainWay, Integer> {
    List<TrainWay> findByCity(City city);
}
