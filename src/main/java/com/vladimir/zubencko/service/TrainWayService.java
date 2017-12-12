package com.vladimir.zubencko.service;

import com.vladimir.zubencko.domain.City;
import com.vladimir.zubencko.domain.Train;
import com.vladimir.zubencko.domain.TrainWay;

import java.time.LocalTime;
import java.util.List;

public interface TrainWayService {
    void save(TrainWay trainWay);
    void delete(TrainWay trainWay);
    void delete(List<TrainWay> trainWays);
    void saveTrainWay(Train train, List<String> cities,
                      List<Integer> departureHour, List<Integer> departureMinute,
                      List<Integer> stoppingHour,List<Integer> stoppingMinute, List<Integer> cost);
    List<Integer> addTimeToCollection(List<String> list, String time);
    List<Integer> isCorrectCost(List<String> costs);
    List<List<Integer>> isCorrectTime(List<String> departureHours, List<String> departureMinutes,
                          List<String> stoppingHours,List<String> stoppingMinutes);
    List<String> isCorrectWay(List<String> cities);
    String isOmit(List<String> cities);
    String checkWay(String trainName, List<String> cities, boolean checkExistsByName);
    List<Object> checkTimeAndCoast(List<String> departureHours, List<String> departureMinutes,
                                   List<String> stoppingHours, List<String> stoppingMinutes,
                                   List<String> costs);
    List<Object> checkTrainWay(String trainName, List<String> cities, List<String> departureHours,
                               List<String> departureMinutes, List<String> stoppingHours,
                               List<String> stoppingMinutes, List<String> costs, boolean checkExistsByName);
    String saveChanges( List<String> cities,  List<Integer> departureHour,  List<Integer> departureMinute,
                        List<Integer> stoppingHour,  List<Integer> stoppingMinute, List<Integer> cost,
                        Train train);
    LocalTime getTime(int index, List<Integer> hours, List<Integer> minutes);
    void deleteOldTrailWay( Train train, List<String> cities);
    List<TrainWay> searchByCity(City city);
}
