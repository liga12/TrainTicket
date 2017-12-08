package com.vladimir.zubencko.service;

import com.vladimir.zubencko.domain.Train;
import com.vladimir.zubencko.domain.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TrainServiceImpl implements TrainService {

    @Autowired
    TrainRepository trainRepository;

    @Override
    public Boolean existsByName(String name) {
        return trainRepository.existsByName(name);
    }

    @Override
    public void save(Train train) {
        trainRepository.saveAndFlush(train);
    }
}
