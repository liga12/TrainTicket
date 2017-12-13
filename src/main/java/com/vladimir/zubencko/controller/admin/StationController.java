package com.vladimir.zubencko.controller.admin;

import com.vladimir.zubencko.domain.*;
import com.vladimir.zubencko.service.StationService;
import com.vladimir.zubencko.service.NeighborStationService;
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
public class StationController {

    private final StationService stationService;
    private final NeighborStationService neighborStationService;
    private final TrainWayService trainWayService;
    private List<NeighborStation> deletedNeighbors;
    private List<Train> deletedTrains;

    @Autowired
    public StationController(StationService stationService, NeighborStationService neighborStationService,
                             TrainWayService trainWayService) {
        this.stationService = stationService;
        this.neighborStationService = neighborStationService;
        this.trainWayService = trainWayService;
    }

    @GetMapping("home")
    public ModelAndView home(@RequestParam(value = "error", required = false) String error,
                             @RequestParam(value = "errorEdit", required = false) String errorEdit,
                             @RequestParam(value = "searchStations", required = false) String searchStations,
                             @RequestParam(value = "stationName", required = false) String stationName,
                             @RequestParam(value = "searchAll", required = false) String searchAll) {
        ModelAndView modelAndView = new ModelAndView("adminStation");
        List<Station> cities = stationService.getStation();
        if (!cities.isEmpty()) {
            modelAndView.addObject("stations", cities);
            modelAndView.addObject("error", error);
        }
        if (searchStations != null) {
            List<Station> citiesSearch = stationService.searchByBitName(searchStations);
            modelAndView.addObject("searchStations", citiesSearch);
        }
        if (searchAll != null) {
            List<Station> citiesSearch = stationService.getAll();
            modelAndView.addObject("searchStations", citiesSearch);
        }
        if (stationName != null) {
            Station station = stationService.searchByFullName(stationName);
            if (station != null) {
                modelAndView.addObject("stationName", stationName);
                List<NeighborStation> neighbors = station.getStations();
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

    @PostMapping("addStation")
    public String addStation(@RequestParam(value = "station", required = false) String stationName,
                          @RequestParam(value = "neighborStation") List<String> neighborCities,
                          RedirectAttributes redirectAttributes) {
        String status = stationService.validStationAndNeighbor(stationName, neighborCities);
        if (!"OK".equals(status)){
            redirectAttributes.addAttribute("error", status);
            return "redirect:/admin/home";
        }
        Station station = new Station(stationName);
        stationService.save(station);
        neighborStationService.saveNeighbors(station, neighborCities);
        return "redirect:/admin/home";
    }

    @PostMapping("searchStation")
    public String searchStation(@RequestParam(value = "station") String stationName,
                             @RequestParam(value = "search", required = false) String search,
                             @RequestParam(value = "searchAll", required = false) String searchAll,
                             RedirectAttributes redirectAttributes) {
        if (!"".equals(stationName)) {
            if (search != null) {
                redirectAttributes.addAttribute("searchStations", stationName);
            }
        }
        if (searchAll != null) {
            redirectAttributes.addAttribute("searchAll", "searchAll");
        }
        return "redirect:/admin/home";
    }

    @GetMapping("deleteStation")
    public String deleteStation(@RequestParam(value = "name") String name,
                             @RequestParam(value = "delete", required = false) String delete,
                             RedirectAttributes redirectAttributes) {
        if (!"".equals(name)) {
            Station station = stationService.searchByFullName(name);
            if (station != null) {
                List<TrainWay> trainWays = trainWayService.searchByStation(station);
                List<Train> trains = stationService.getStationLink(station, trainWays);
                if (delete != null || trains.isEmpty()) {
                    stationService.deleteStationLink(station, trainWays, trains);
                } else {
                    List<String> nameTrains = new ArrayList<>();
                    for (Train train : trains) {
                        nameTrains.add(train.getName());
                    }
                    redirectAttributes.addAttribute("station", name);
                    redirectAttributes.addAttribute("nameTrains", nameTrains);
                    return "redirect:/admin/checkDelete";
                }
            }
        }
        return "redirect:/admin/home";
    }

    @GetMapping("checkDelete")
    public ModelAndView checkDelete(@RequestParam(value = "station") String station,
                                    @RequestParam(value = "nameTrains") List<String> trains) {
        return stationService.prepareDate(station, trains,
                "/admin/deleteStation?name="+station+"&delete=true", "delete");
    }

    @GetMapping("editStation")
    public String editStation(@RequestParam(value = "name") String name,
                           RedirectAttributes redirectAttributes) {
        if (!"".equals(name)) {
            redirectAttributes.addAttribute("stationName", name);
        }
        return "redirect:/admin/home";
    }

    @PostMapping("saveEditStation")
    public String saveEditStation(@RequestParam(value = "station") String stationName,
                               @RequestParam(value = "stationOriginal") String stationOriginal,
                               @RequestParam(value = "neighborStationEdit") List<String> neighborCities,
                               RedirectAttributes redirectAttributes) {
        String status = stationService.validEditStationAndNeighbor(stationName,stationOriginal,neighborCities);
        if (!"OK".equals(status)){
            if (!"home".equals(status)){
                redirectAttributes.addAttribute("errorEdit", status);
            }
            return "redirect:/admin/home";
        }
        Station station1 = stationService.searchByFullName(stationOriginal);
        if (station1 == null) {
            redirectAttributes.addAttribute("errorEdit", "Not correct data");
            return "redirect:/admin/home";
        }
        station1.setName(stationName);
        neighborStationService.saveEditNeighbors(station1, neighborCities);
        deletedNeighbors = neighborStationService.deleteEditNeighbor(station1, neighborCities);
        deletedTrains = neighborStationService.checkEditStationLink(deletedNeighbors, station1);
        if (!deletedTrains.isEmpty()) {
            redirectAttributes.addAttribute("neighborStation", stationName);
            List<String> nameTrains = new ArrayList<>();
            for (Train deletedTrain : deletedTrains) {
                nameTrains.add(deletedTrain.getName());
            }
            redirectAttributes.addAttribute("neighborStation", stationName);
            redirectAttributes.addAttribute("nameTrains", nameTrains);
            return "redirect:/admin/checkEdit";
        }
        neighborStationService.delete(deletedNeighbors);
        return "redirect:/admin/home";
    }

    @GetMapping("checkEdit")
    public ModelAndView checkEdit(@RequestParam(value = "neighborStation") String station,
                                  @RequestParam(value = "nameTrains") List<String> trains) {
        return stationService.prepareDate(station, trains, "/admin/deleteEditNeighbor?status=true", "edit");
    }

    @GetMapping("deleteEditNeighbor")
    public String deleteEditNeighbor(@RequestParam(value = "status") String status) {
        if (status != null) {
            neighborStationService.deleteOldNeighbor(deletedNeighbors, deletedTrains);
        }
        return "redirect:/admin/home";

    }
}
