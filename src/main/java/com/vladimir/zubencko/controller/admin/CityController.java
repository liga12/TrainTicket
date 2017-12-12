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
    private List<NeighborCity> deletedNeighbors;
    private List<Train> deletedTrains;

    @Autowired
    public CityController(CityService cityService, NeighborCityService neighborCityService,
                          TrainWayService trainWayService) {
        this.cityService = cityService;
        this.neighborCityService = neighborCityService;
        this.trainWayService = trainWayService;
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
        String status =cityService.validCityAndNeighbor(cityName, neighborCities);
        if (!"OK".equals(status)){
            redirectAttributes.addAttribute("error", status);
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
                List<Train> trains = cityService.getCityLink(city, trainWays);
                if (delete != null || trains.isEmpty()) {
                    cityService.deleteCityLink(city, trainWays, trains);
                } else {
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
                                    @RequestParam(value = "nameTrains") List<String> trains) {
        return cityService.prepareDate(city, trains,
                "/admin/deleteCity?name="+city+"&delete=true", "delete");
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
        String status = cityService.validEditCityAndNeighbor(cityName,cityOriginal,neighborCities);
        if (!"OK".equals(status)){
            if (!"home".equals(status)){
                redirectAttributes.addAttribute("errorEdit", status);
            }
            return "redirect:/admin/home";
        }
        City city1 = cityService.searchByFullName(cityOriginal);
        if (city1 == null) {
            redirectAttributes.addAttribute("errorEdit", "Not correct data");
            return "redirect:/admin/home";
        }
        city1.setName(cityName);
        neighborCityService.saveEditNeighbors(city1, neighborCities);
        deletedNeighbors = neighborCityService.deleteEditNeighbor(city1, neighborCities);
        deletedTrains = neighborCityService.checkEditCityLink(deletedNeighbors, city1);
        if (!deletedTrains.isEmpty()) {
            redirectAttributes.addAttribute("city", cityName);
            List<String> nameTrains = new ArrayList<>();
            for (Train deletedTrain : deletedTrains) {
                nameTrains.add(deletedTrain.getName());
            }
            redirectAttributes.addAttribute("city", cityName);
            redirectAttributes.addAttribute("nameTrains", nameTrains);
            return "redirect:/admin/checkEdit";
        }
        neighborCityService.delete(deletedNeighbors);
        return "redirect:/admin/home";
    }

    @GetMapping("checkEdit")
    public ModelAndView checkEdit(@RequestParam(value = "city") String city,
                                  @RequestParam(value = "nameTrains") List<String> trains) {
        return cityService.prepareDate(city, trains, "/admin/deleteEditNeighbor?status=true", "edit");
    }

    @GetMapping("deleteEditNeighbor")
    public String deleteEditNeighbor(@RequestParam(value = "status") String status) {
        if (status != null) {
            neighborCityService.deleteOldNeighbor(deletedNeighbors, deletedTrains);
        }
        return "redirect:/admin/home";

    }
}
