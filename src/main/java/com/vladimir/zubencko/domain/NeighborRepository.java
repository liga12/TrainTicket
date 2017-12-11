package com.vladimir.zubencko.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NeighborRepository extends JpaRepository<NeighborCity, Integer> {
    NeighborCity findById(Integer id);
}
