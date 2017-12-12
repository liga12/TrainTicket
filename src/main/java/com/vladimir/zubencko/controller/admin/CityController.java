package com.vladimir.zubencko.controller.admin;

import com.vladimir.zubencko.domain.*;
import com.vladimir.zubencko.service.CityService;
import com.vladimir.zubencko.service.NeighborCityService;
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
public class CityController {

    private final CityService cityService;
    private final NeighborCityService neighborCityService;
    private final TrainWayService trainWayService;
    private final TrainService trainService;

    @Autowired
    public CityController(CityService cityService, NeighborCityService neighborCityService,
                          TrainWayService trainWayService, TrainService trainService) {
        this.cityService = cityService;
        this.neighborCityService = neighborCityService;
        this.trainWayService = trainWayService;
        this.trainService = trainService;

    }

    @GetMapping("home")
    public ModelAndView home(@RequestParam(value = "error", required = false) String error,
                             @RequestParam(value = "errorEdit", required = false) String errorEdit,
                             @RequestParam(value = "searchCities", required = false) String searchCities,
                             @RequestParam(value = "cityName", required = false) String cityName,
                             @RequestParam(value = "searchAll", required = false) String searchAll) {
        ModelAndView modelAndView = new ModelAndView("adminCity");
        List<City> cities = cityService.getCities();
        if (!cities.isEmpty()) {
            modelAndView.addObject("cities", cities);
            modelAndView.addObject("error", error);
        }
        if (searchCities != null) {
            List<City> citiesSearch = cityService.searchByBitName(searchCities);
            modelAndView.addObject("searchCities", citiesSearch);
        }
        if (searchAll != null) {
            List<City> citiesSearch = cityService.getAll();
            modelAndView.addObject("searchCities", citiesSearch);
        }
        if (cityName != null) {
            City city = cityService.searchByFullName(cityName);
            if (city != null) {
                modelAndView.addObject("cityName", cityName);
                List<NeighborCity> neighbors = city.getCities();
                if (neighbors != null && !neighbors.isEmpty()) {
                    modelAndView.addObject("neighbors", neighbors);
                }
            }
        }
        if (errorEdit != null) {
            modelAndView.addObject("errorEdit", errorEdit);
        }
        return modelAndView;
    }

    @PostMapping("addCity")
    public String addCity(@RequestParam(value = "city", required = false) String cityName,
                          @RequestParam(value = "neighborCity") List<String> neighborCities,
                          RedirectAttributes redirectAttributes) {
        int a=0;
        a++;

        if (!"".equals(cityName)) {
            if (!cityService.existsByName(cityName)) {
                if (neighborCities != null) {
                    boolean result = neighborCityService.isEqual(neighborCities);
                    if (result) {
                        redirectAttributes.addAttribute("error", "Neighbor cities is equals");
                        return "redirect:/admin/home";
                    }
                } else {
                    redirectAttributes.addAttribute("error", "Neighbor cities is empty");
                    return "redirect:/admin/home";
                }
            } else {
                redirectAttributes.addAttribute("error", "City is exist");
                return "redirect:/admin/home";
            }
        } else {
            redirectAttributes.addAttribute("error", "City is empty");
            return "redirect:/admin/home";
        }
        City city = new City(cityName);
        cityService.save(city);
        neighborCityService.saveNeighbors(city, neighborCities);
        return "redirect:/admin/home";
    }

    @PostMapping("searchCity")
    public String searchCity(@RequestParam(value = "city") String cityName,
                             @RequestParam(value = "search", required = false) String search,
                             @RequestParam(value = "searchAll", required = false) String searchAll,
                             RedirectAttributes redirectAttributes) {
        if (!"".equals(cityName)) {
            if (search != null) {
                redirectAttributes.addAttribute("searchCities", cityName);
            }
        }
        if (searchAll != null) {
            redirectAttributes.addAttribute("searchAll", "searchAll");
        }
        return "redirect:/admin/home";
    }

    @GetMapping("deleteCity")
    public String deleteCity(@RequestParam(value = "name") String name,
                             @RequestParam(value = "delete", required = false) String delete,
                             RedirectAttributes redirectAttributes) {
        if (!"".equals(name)) {
            City city = cityService.searchByFullName(name);
            if (city != null) {
                List<TrainWay> trainWays = trainWayService.searchByCity(city);
                List<Train> trains = cityService.getCityLink(city,trainWays);
                if (delete!=null||trains.isEmpty()){
                    cityService.deleteCityLink(city,trainWays,trains);
                }else {
                    List<String> nameTrains = new ArrayList<>();
                    for (Train train : trains) {
                        nameTrains.add(train.getName());
                    }
                    redirectAttributes.addAttribute("city", name);
                    redirectAttributes.addAttribute("nameTrains", nameTrains);
                    return "redirect:/admin/checkDelete";
                }
            }
        }
        return "redirect:/admin/home";
    }

    @GetMapping("checkDelete")
    public ModelAndView checkDelete(@RequestParam(value = "city") String city,
                                    @RequestParam(value = "nameTrains") List<String> trains){
        ModelAndView modelAndView = new ModelAndView("deleteCity");
        if (city!=null&&trains!=null){
            if (!trains.isEmpty()){
                List<Train> trains1 = new ArrayList<>();
                for (String trainName : trains) {
                    Train train = trainService.searchByFullName(trainName);
                    if (train!=null){
                        trains1.add(train);
                    }
                }

                modelAndView.addObject("city", city);
                modelAndView.addObject("trains", trains1);
            }

        }
        return modelAndView;

    }


    @GetMapping("editCity")
    public String editCity(@RequestParam(value = "name") String name,
                           RedirectAttributes redirectAttributes) {
        if (!"".equals(name)) {
            redirectAttributes.addAttribute("cityName", name);
        }
        return "redirect:/admin/home";
    }

    @PostMapping("saveEditCity")
    public String saveEditCity(@RequestParam(value = "city") String cityName,
                               @RequestParam(value = "cityOriginal") String cityOriginal,
                               @RequestParam(value = "neighborCityEdit") List<String> neighborCities,
                               RedirectAttributes redirectAttributes) {
        if (!"".equals(cityName)) {
            boolean equalsCity;
            if (cityOriginal != null) {
                if (cityName.equals(cityOriginal)) {
                    equalsCity = false;
                } else {
                    equalsCity = cityService.existsByName(cityName);
                }
            } else {
                return "redirect:/admin/home";
            }
            if (!equalsCity) {
                if (neighborCities != null) {
                    String result = neighborCityService.checkNeighbors(neighborCities, cityName);
                    if (!result.equals("OK")) {
                        redirectAttributes.addAttribute("errorEdit", result);
                        return "redirect:/admin/home";
                    }
                } else {
                    redirectAttributes.addAttribute("errorEdit", "City is exist");
                    return "redirect:/admin/home";
                }
            }
        } else {
            redirectAttributes.addAttribute("errorEdit", "City is empty");
            return "redirect:/admin/home";
        }
        City city1 = cityService.searchByFullName(cityOriginal);
        if (city1 == null) {
            redirectAttributes.addAttribute("errorEdit", "Not correct data");
            return "redirect:/admin/home";
        }
        city1.setName(cityName);
        neighborCityService.saveEditNeighbors(city1, neighborCities);
        return "redirect:/admin/home";
    }
}
