package com.vladimir.zubencko.service;

import com.vladimir.zubencko.domain.*;
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
    public void saveTrainWay(Train train, List<String> cities, List<Integer> arrivalHour, List<Integer> arrivalMinute, List<Integer> stoppingHours, List<Integer> stoppingMinute, List<Integer> coast) {
        for (int i = 0; i < cities.size(); i++) {
            String cityName = cities.get(i);
            if (!cityName.equals("None") && coast.get(i) != null) {
                City city = cityService.searchByFullName(cityName);
                if (city != null) {
                    TrainWay trainWay = new TrainWay(train, city,
                            LocalTime.now().withHour(arrivalHour.get(i)).withMinute(arrivalMinute.get(i)),
                            LocalTime.now().withHour(stoppingHours.get(i)).withMinute(stoppingMinute.get(i)), coast.get(i));
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
                    return null;
                }
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return times;
    }

    public List<List<Integer>> isCorrectTime(List<String> departureHours, List<String> departureMinutes,
                                             List<String> stoppingHours, List<String> stoppingMinutes) {
        List<List<Integer>> lists = null;
        List<Integer> departureHour = addTimeToCollection(departureHours, "hour");
        List<Integer> departureMinute = addTimeToCollection(departureMinutes, "minute");
        List<Integer> stoppingHour = addTimeToCollection(stoppingHours, "hour");
        List<Integer> stoppingMinute = addTimeToCollection(stoppingMinutes, "minute");
        boolean result = !(departureHour == null || departureMinute == null
                || stoppingHour == null || stoppingMinute == null);
        if (result) {
            lists = new ArrayList<>();
            lists.add(departureHour);
            lists.add(departureMinute);
            lists.add(stoppingHour);
            lists.add(stoppingMinute);
        }
        return lists;
    }

    @Override
    public List<Integer> isCorrectCost(List<String> costs) {
        List<Integer> list = new ArrayList<>();
        try {
            for (String currentCost : costs) {
                if (currentCost.equals("")) {
                    currentCost = "0";
                }
                int cost = Integer.valueOf(currentCost);
                if (cost < 0) {
                    return new ArrayList<>();
                }
                list.add(cost);
            }

        } catch (NumberFormatException e) {
            return null;
        }
        return list;
    }

    @Override
    public List<String> isCorrectWay(List<String> cities) {
        List<String> list = new ArrayList<>();
        int sumEmptyCities = 0;
        for (int i = 0; i < cities.size(); i++) {
            if (cities.get(i).equals("None")) {
                sumEmptyCities++;
            } else {
                if (i != cities.size() - 1 && !cities.get(i + 1).equals("None")) {
                    City city = cityService.searchByFullName(cities.get(i));
                    if (city == null) {
                        list.add("City not exist");
                        return list;
                    }
                    List<NeighborCity> neighborCities = city.getNeighborCities();
                    City neighbor = cityService.searchByFullName(cities.get(i + 1));
                    System.out.println(neighbor.getName());
                    boolean existCity = false;
                    for (NeighborCity neighborCity : neighborCities) {
                        if (neighborCity.getNeighborCity().equals(neighbor)) {
                            System.out.println(neighborCity.getNeighborCity().getName());
                            existCity = true;
                            break;
                        }
                    }
                    if (!existCity) {
                        list.add("Not correct way");
                        return list;
                    }
                }
            }
        }
        list.add(String.valueOf(sumEmptyCities));
        list.add(String.valueOf(sumEmptyCities));
        return list;
    }

    @Override
    public String isOmit(List<String> cities) {
        boolean none = false;
        boolean noNone = true;
        for (int i = 0; i < cities.size(); i++) {
            if (cities.get(i).equals("None")) {
                if (!none) {
                    return "City in the way is omitted";
                }
                noNone = false;
                continue;
            } else {
                if (!noNone) {
                    return "City in the way is omitted";
                }
                none = true;
                for (int j = 0; j < cities.size(); j++) {
                    if (j == i) {
                        continue;
                    } else {
                        if (cities.get(i).equals(cities.get(j))) {
                            return "City is equals";
                        }
                    }
                }
            }
        }
        return "OK";
    }

    @Override
    public String checkWay(String trainName, List<String> cities) {
        if (!trainService.existsByName(trainName)) {
            if (cities == null) {
                return "Cities is empty";
            }
            List<String> correctWay = isCorrectWay(cities);
            if (correctWay.size() != 2) {
                return correctWay.get(0);
            }
            int sumEmptyCities = Integer.valueOf(correctWay.get(1));
            if (sumEmptyCities != cities.size()) {
                String omitWay = isOmit(cities);
                if (!omitWay.equals("OK")) {
                    return omitWay;
                }
            }
        } else {
            return "Train is exist";
        }
        return "OK";
    }

    @Override
    public List<Object> checkTimeAndCoast(List<String> departureHours, List<String> departureMinutes,
                                          List<String> stoppingHours, List<String> stoppingMinutes,
                                          List<String> costs) {
        List<Object> list = new ArrayList<>();
        List<List<Integer>> result;
        if (departureHours == null || departureMinutes == null || stoppingHours == null || stoppingMinutes == null) {
            list.add("Time is empty");
            return list;
        } else {
            result = isCorrectTime(departureHours, departureMinutes, stoppingHours, stoppingMinutes);
            if (result == null) {
                list.add("Not correct time");
                return list;
            }
        }
        List<Integer> cost;
        if (costs != null) {
            List<Integer> costInteger = isCorrectCost(costs);
            if (costInteger == null) {
                list.add("Not correct cost");
                return list;
            } else {
                cost = costInteger;
            }
        } else {
            list.add("Cost is empty");
            return list;

        }
        list.add(result.get(0));
        list.add(result.get(1));
        list.add(result.get(2));
        list.add(result.get(3));
        list.add(cost);
        return list;
    }

}

