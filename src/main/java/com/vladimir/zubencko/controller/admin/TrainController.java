package com.vladimir.zubencko.controller.admin;

import com.vladimir.zubencko.domain.City;
import com.vladimir.zubencko.domain.NeighborCity;
import com.vladimir.zubencko.domain.Train;
import com.vladimir.zubencko.domain.TrainWay;
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

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("admin")
public class TrainController {

    private final TrainService trainService;
    private final TrainWayService trainWayService;
    private final CityService cityService;

    @Autowired
    public TrainController(CityService cityService, TrainService trainService, TrainWayService trainWayService) {
        this.cityService = cityService;
        this.trainService = trainService;
        this.trainWayService = trainWayService;
    }

    @GetMapping("train")
    public ModelAndView home(@RequestParam(value = "error", required = false) String error,
                             @RequestParam(value = "searchTrains", required = false) String searchTrains,
                             @RequestParam(value = "searchAll", required = false) String searchAll,
                             @RequestParam(value = "trainName", required = false) String trainName,
                             @RequestParam(value = "errorEdit", required = false) String errorEdit) {
        ModelAndView modelAndView = new ModelAndView("adminTrain");
        List<City> cities = cityService.getCities();
        if (!cities.isEmpty()) {
            modelAndView.addObject("cities", cities);
            modelAndView.addObject("error", error);
        }
        if (searchTrains != null) {
            List<Train> trainsSearch = trainService.searchByBitName(searchTrains);
            modelAndView.addObject("searchTrains", trainsSearch);
        }
        if (searchAll != null) {
            List<Train> trainsSearch = trainService.getAll();
            modelAndView.addObject("searchTrains", trainsSearch);
        }
        if (trainName != null) {
            Train train = trainService.searchByFullName(trainName);
            if (train != null) {
                modelAndView.addObject("trainName", trainName);
                List<TrainWay> trainWays = train.getTrainWays();
                if (trainWays != null && !trainWays.isEmpty()) {
                    modelAndView.addObject("trainWays", trainWays);
                }
            }
        }
        if (errorEdit != null) {
            modelAndView.addObject("errorEdit", errorEdit);
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
        List<Object> result = trainWayService.checkTrainWay(trainName, cities, departureHours, departureMinutes,
                stoppingHours, stoppingMinutes, costs, true);
        if (result.size() == 1) {
            String error = (String) result.get(0);
            redirectAttributes.addAttribute("error", error);
            return "redirect:/admin/train";
        }
        List<Integer> departureHour = (List<Integer>) result.get(0);
        List<Integer> departureMinute = (List<Integer>) result.get(1);
        List<Integer> stoppingHour = (List<Integer>) result.get(2);
        List<Integer> stoppingMinute = (List<Integer>) result.get(3);
        List<Integer> cost = (List<Integer>) result.get(4);
        Train train = new Train(trainName);
        trainService.save(train);
        trainWayService.saveTrainWay(train, cities, departureHour, departureMinute, stoppingHour, stoppingMinute, cost);
        return "redirect:/admin/train";
    }

    @PostMapping("searchTrain")
    public String searchCity(@RequestParam(value = "train") String trainName,
                             @RequestParam(value = "search", required = false) String search,
                             @RequestParam(value = "searchAll", required = false) String searchAll,
                             RedirectAttributes redirectAttributes) {
        if (trainName != null && !trainName.equals("")) {
            if (search != null) {
                redirectAttributes.addAttribute("searchTrains", trainName);
            }
        }
        if (searchAll != null) {
            redirectAttributes.addAttribute("searchAll", "searchAll");
        }
        return "redirect:/admin/train";
    }

    @GetMapping("deleteTrain")
    public String deleteCity(@RequestParam(value = "name") String name) {
        if (!"".equals(name)) {
            Train train = trainService.searchByFullName(name);
            if (train != null) {
                trainService.delete(train);
            }
        }
        return "redirect:/admin/train";
    }

    @GetMapping("editTrain")
    public String editCity(@RequestParam(value = "name") String name,
                           RedirectAttributes redirectAttributes) {
        if (name != null && !name.equals("")) {
            redirectAttributes.addAttribute("trainName", name);
        }
        return "redirect:/admin/train";
    }

    @PostMapping("saveEditTrainWay")
    public String saveEditTrain(@RequestParam(value = "train") String trainName,
                                @RequestParam(value = "trainOriginal") String trainOriginal,
                                @RequestParam(value = "city") List<String> cities,
                                @RequestParam(value = "departureHour") List<String> departureHours,
                                @RequestParam(value = "departureMinute") List<String> departureMinutes,
                                @RequestParam(value = "stoppingHour") List<String> stoppingHours,
                                @RequestParam(value = "stoppingMinute") List<String> stoppingMinutes,
                                @RequestParam(value = "cost") List<String> costs,
                                RedirectAttributes redirectAttributes) {
        if ("".equals(trainOriginal)) {
            redirectAttributes.addAttribute("errorEdit", "Train is empty");
            return "redirect:/admin/train";
        }
        List<Object> result = trainWayService.checkTrainWay(trainName, cities, departureHours, departureMinutes,
                stoppingHours, stoppingMinutes, costs, false);
        if (result.size() == 1) {
            String error = (String) result.get(0);
            redirectAttributes.addAttribute("errorEdit", error);
            return "redirect:/admin/train";
        }
        List<Integer> departureHour = (List<Integer>) result.get(0);
        List<Integer> departureMinute = (List<Integer>) result.get(1);
        List<Integer> stoppingHour = (List<Integer>) result.get(2);
        List<Integer> stoppingMinute = (List<Integer>) result.get(3);
        List<Integer> cost = (List<Integer>) result.get(4);
        Train train = trainService.searchByFullName(trainOriginal);
        if (train == null) {
            redirectAttributes.addAttribute("errorEdit", "Not correct data");
            return "redirect:/admin/home";
        }
        train.setName(trainName);
        trainService.save(train);
        String saver = trainWayService.saveChanges(cities, departureHour,departureMinute,
                stoppingHour, stoppingMinute, cost,train);
        if (saver!=null){
            redirectAttributes.addAttribute("errorEdit", saver);
            return "redirect:/admin/train" ;
        }
        trainWayService.deleteOldTrailWay(train, cities);
        return "redirect:/admin/train";
    }
}
