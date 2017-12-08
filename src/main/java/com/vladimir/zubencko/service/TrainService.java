package com.vladimir.zubencko.service;

import com.vladimir.zubencko.domain.Train;

public interface TrainService {
    Boolean existsByName(String name);
    void save(Train train);
}
