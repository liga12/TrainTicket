package com.vladimir.zubencko.service;

import com.vladimir.zubencko.domain.Train;
import com.vladimir.zubencko.domain.TrainWay;

import java.util.List;

public interface TrainWayService {
    void save(TrainWay trainWay);
    void saveTrainWay(Train train, List<String> cities,
                      List<Integer> departureHour, List<Integer> departureMinute,
                      List<Integer> stoppingHour,List<Integer> stoppingMinute, List<Integer> cost);
    List<Integer> addTimeToCollection(List<String> list, String time);
    List<Integer> isCorrectCost(List<String> costs);
    List<List<Integer>> isCorrectTime(List<String> departureHours, List<String> departureMinutes,
                          List<String> stoppingHours,List<String> stoppingMinutes);
    List<String> isCorrectWay(List<String> cities);
    String isOmit(List<String> cities);
    String checkWay(String trainName, List<String> cities);
    List<Object> checkTimeAndCoast(List<String> departureHours, List<String> departureMinutes,
                                   List<String> stoppingHours, List<String> stoppingMinutes,
                                   List<String> costs);
}
