package com.vladimir.zubencko.service;

import com.vladimir.zubencko.domain.City;
import com.vladimir.zubencko.domain.Train;
import com.vladimir.zubencko.domain.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void save(Train train) {
        trainRepository.saveAndFlush(train);
    }

    @Override
    public List<Train> searchByBitName(String name) {
        return trainRepository.findByNameContaining(name);
    }

    @Override
    @Transactional
    public void delete(Train train) {
        trainRepository.delete(train);
    }

    @Override
    @Transactional
    public void delete(List<Train> trains) {
        trainRepository.delete(trains);
    }

    @Override
    public List<Train> getAll() {
        return trainRepository.findAll();
    }

    @Override
    public Train searchByFullName(String name) {
        return trainRepository.findByName(name);
    }
}
