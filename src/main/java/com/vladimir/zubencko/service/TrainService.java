package com.vladimir.zubencko.service;

import com.vladimir.zubencko.domain.Train;
import com.vladimir.zubencko.domain.TrainWay;

import java.util.List;

public interface TrainService {
    Boolean existsByName(String name);
    void save(Train train);
    List<Train> searchByBitName(String name);
    void delete(Train train);
    void delete(List<Train> trains);
    List<Train> getAll();
    Train searchByFullName(String name);
}
