package com.vladimir.zubencko.controller.admin;

import com.vladimir.zubencko.domain.City;
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
                             @RequestParam(value = "searchAll", required = false) String searchAll) {
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
        if (trainName != null && !trainName.equals("")) {
            String result = trainWayService.checkWay(trainName, cities);
            if (!result.equals("OK")) {
                redirectAttributes.addAttribute("error", result);
                return "redirect:/admin/train";
            }
        } else {
            redirectAttributes.addAttribute("error", "Train is empty");
            return "redirect:/admin/train";
        }
        List<Integer> departureHour;
        List<Integer> departureMinute;
        List<Integer> stoppingHour;
        List<Integer> stoppingMinute;
        List<Integer> cost;
        List<Object> list = trainWayService.checkTimeAndCoast
                (departureHours, departureMinutes, stoppingHours, stoppingMinutes, costs);
        if (list.size() == 1) {
            redirectAttributes.addAttribute("error", list.get(0));
            return "redirect:/admin/train";
        }
        departureHour = (List<Integer>) list.get(0);
        departureMinute = (List<Integer>) list.get(1);
        stoppingHour = (List<Integer>) list.get(2);
        stoppingMinute = (List<Integer>) list.get(3);
        cost = (List<Integer>) list.get(4);
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
        if (trainName!=null&&!trainName.equals("")) {
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
        if (name!=null&&!name.equals("")) {
            Train train = trainService.searchByFullName(name);
            if (train != null) {
                trainService.delete(train);
            }
        }
        return "redirect:/admin/train";
    }
}
