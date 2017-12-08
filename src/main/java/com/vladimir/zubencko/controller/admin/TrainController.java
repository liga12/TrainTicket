package com.vladimir.zubencko.controller.admin;

import com.vladimir.zubencko.domain.City;
import com.vladimir.zubencko.domain.NeighborCity;
import com.vladimir.zubencko.domain.Train;
import com.vladimir.zubencko.service.CityService;
import com.vladimir.zubencko.service.TrainService;
import com.vladimir.zubencko.service.TrainWayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("admin")
public class TrainController {

    @Autowired
    CityService cityService;

    @Autowired
    TrainService trainService;

    @Autowired
    TrainWayService trainWayService;

    @GetMapping("train")
    public ModelAndView main(@RequestParam(value = "error", required = false) String error) {
        ModelAndView modelAndView = new ModelAndView("adminTrain");
        List<City> cities = cityService.getCities();
        if (!cities.isEmpty()) {
            modelAndView.addObject("cities", cities);
            modelAndView.addObject("error", error);
            return modelAndView;
        }
        return modelAndView;
    }

    @PostMapping("addTrain")
    public String addTrain(@RequestParam(value = "train") String trainName,
                           @RequestParam(value = "city") List<String> cities,
                           @RequestParam(value = "departureHour") List<String> departureHours,
                           @RequestParam(value = "departureMinute") List<String> departureMinutes,
                           @RequestParam(value = "stoppingHour") List<String> stoppingHours,
                           @RequestParam(value = "stoppingMinute") List<String> stoppingMinutes,
                           @RequestParam(value = "cost") List<String> costs,
                           RedirectAttributes redirectAttributes) {
        if (!trainName.equals("")) {
            if (!trainService.existsByName(trainName)) {
                int sum = 0;
                for (int i = 0; i < cities.size(); i++) {
                    if (cities.get(i).equals("None")) {
                        sum++;
                    } else {
                        if (i != cities.size() - 1&&!cities.get(i+1).equals("None")) {
                            City city = cityService.searchByFullName(cities.get(i));
                            if (city==null){
                                redirectAttributes.addAttribute("error", "City not exist");
                                return "redirect:/admin/train";
                            }
                            List<NeighborCity> neighborCities = city.getNeighborCities();
                            City neighbor = cityService.searchByFullName(cities.get(i + 1));
                            System.out.println(neighbor.getName());
                            boolean existCity = false;
                            for (NeighborCity neighborCity : neighborCities) {
                                if (neighborCity.getNeighborCity().equals(neighbor))
                                {System.out.println(neighborCity.getNeighborCity().getName());
                                    existCity = true;
                                    break;
                                }
                            }
                            if (!existCity){
                                redirectAttributes.addAttribute("error", "Not correct way");
                                return "redirect:/admin/train";
                            }
                        }
                    }
                }
                if (sum != 10) {
                    boolean none = false;
                    boolean noNone = true;
                    for (int i = 0; i < cities.size(); i++) {
                        if (cities.get(i).equals("None")) {
                            if (!none) {
                                redirectAttributes.addAttribute("error", "City in the way is omitted");
                                return "redirect:/admin/train";
                            }
                            noNone = false;
                            continue;
                        } else {
                            if (!noNone) {
                                redirectAttributes.addAttribute("error", "City in the way is omitted");
                                return "redirect:/admin/train";
                            }
                            none = true;

                            for (int j = 0; j < cities.size(); j++) {
                                if (j == i) {
                                    continue;
                                } else {
                                    if (cities.get(i).equals(cities.get(j))) {
                                        redirectAttributes.addAttribute("error", "City is equals");
                                        return "redirect:/admin/train";
                                    }
                                }
                            }
                        }
                    }

                } else {

                }

            } else {
                redirectAttributes.addAttribute("error", "Train is exist");
                return "redirect:/admin/train";
            }
        } else {
            redirectAttributes.addAttribute("error", "Train is empty");
            return "redirect:/admin/train";
        }
        List<Integer> departureHour = trainWayService.addTimeToCollection(departureHours, "hour");
        if (departureHour == null) {
            redirectAttributes.addAttribute("error", "Not correct time");
            return "redirect:/admin/train";
        }
        List<Integer> departureMinute = trainWayService.addTimeToCollection(departureMinutes, "minute");
        if (departureMinute.isEmpty()) {
            redirectAttributes.addAttribute("error", "Not correct time");
            return "redirect:/admin/train";
        }
        List<Integer> stoppingHour = trainWayService.addTimeToCollection(stoppingHours, "hour");
        if (stoppingHour.isEmpty()) {
            redirectAttributes.addAttribute("error", "Not correct time");
            return "redirect:/admin/train";
        }
        List<Integer> stoppingMinute = trainWayService.addTimeToCollection(stoppingMinutes, "minute");
        if (stoppingMinute.isEmpty()) {
            redirectAttributes.addAttribute("error", "Not correct time");
            return "redirect:/admin/train";
        }
        List<Integer> cost = trainWayService.isCorrectCost(costs);
        if (cost.isEmpty()) {
            redirectAttributes.addAttribute("error", "Not correct cost");
            return "redirect:/admin/train";
        }
        Train train = new Train(trainName.toLowerCase());
        trainService.save(train);
        trainWayService.saveTrainWay(train, cities, departureHour, departureMinute, stoppingHour, stoppingMinute, cost);
        return "redirect:/admin/train";
    }
}
