package com.vladimir.zubencko.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NeighborStationRepository extends JpaRepository<NeighborStation, Integer> {
    NeighborStation findById(Integer id);
}
