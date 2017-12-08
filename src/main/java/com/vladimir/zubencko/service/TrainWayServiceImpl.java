package com.vladimir.zubencko.service;

import com.vladimir.zubencko.domain.City;
import com.vladimir.zubencko.domain.Train;
import com.vladimir.zubencko.domain.TrainWay;
import com.vladimir.zubencko.domain.TrainWayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrainWayServiceImpl implements TrainWayService {

    @Autowired
    TrainWayRepository trainWayRepository;

    @Autowired
    TrainService trainService;

    @Autowired
    CityService cityService;

    @Override
    public void save(TrainWay trainWay) {
        trainWayRepository.saveAndFlush(trainWay);

    }

    @Override
    public void saveTrainWay(Train train, List<String> cities, List<Integer> arrivalHour, List<Integer> arrivalMinute,List<Integer> stoppingHours, List<Integer> stoppingMinute, List<Integer> coast) {
        for (int i = 0; i < cities.size(); i++) {
            String cityName = cities.get(i);
            if (!cityName.equals("None")&&coast.get(i)!=null) {
                City city = cityService.searchByFullName(cityName);
                if (city!=null){
                    TrainWay trainWay = new TrainWay(train,city,
                            LocalTime.now().withHour(arrivalHour.get(i)).withMinute(arrivalMinute.get(i)),
                            LocalTime.now().withHour(stoppingHours.get(i)).withMinute(stoppingMinute.get(i)),coast.get(i));
                    save(trainWay);
                }
            } else {
                break;
            }
        }
    }

    @Override
    public List<Integer> addTimeToCollection(List<String> list, String time) {
        List<Integer> times = new ArrayList<>();
        int startTime = -1;
        int finishTime;
        switch (time) {
            case "hour":
                finishTime = 25;
                break;
            case "minute":
                finishTime = 61;
                break;
            default:
                return new ArrayList<>();
        }
        try {
            for (String s : list) {
                int currentTime = Integer.valueOf(s);
                if (startTime < currentTime && currentTime < finishTime) {
                    times.add(currentTime);
                } else {
                    return new ArrayList<>();
                }
            }
        } catch (NumberFormatException e) {
            return new ArrayList<>();
        }
        return times;
    }

    @Override
    public List<Integer> isCorrectCost(List<String> costs){
        List <Integer> list = new ArrayList<>();
        try{
            for (String currentCost : costs) {
                if (currentCost.equals("")){
                    currentCost="0";
                }
                int cost = Integer.valueOf(currentCost);
                if (cost<0){
                    return new ArrayList<>();
                }
                list.add(cost);
            }

        }catch (NumberFormatException e){
            return new ArrayList<>();
        }
        return list;
    }

}

