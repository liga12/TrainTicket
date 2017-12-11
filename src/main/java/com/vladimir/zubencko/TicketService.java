package com.vladimir.zubencko;

import com.vladimir.zubencko.domain.City;
import com.vladimir.zubencko.domain.Train;
import com.vladimir.zubencko.domain.TrainWay;

import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class TicketService {

    private City startCity;
    private City finishCity;
    private List<City> cities;
    private int peresadka;
    private LocalDateTime dateTime;

    public TicketService(City startCity, City finishCity, int peresadka) {
        this.startCity = startCity;
        this.finishCity = finishCity;
        this.cities = cities;
        this.peresadka = peresadka;
        dateTime = LocalDateTime.now().withSecond(0);
    }

    private boolean val(City startCity, City finishCity) {
        if (startCity.equals(finishCity)) {
            System.out.println("odinakov");
            return false;
        }
        return true;
    }

    public Way test() throws CloneNotSupportedException, IOException, ClassNotFoundException {
        Way way = new Way();
        if (val(startCity, finishCity)) {
            way.setWays(new ArrayList<>());
            calculateFirstRouts(startCity, finishCity, way);
            while (!isFinished(way)) {
                calculateWay(finishCity, way, peresadka);

//                new Main().printWays(way);
                if (way.getWays().size() == 0) {
                    return null;
                }
            }
            if (way.getWays().size() == 0) {
                return null;
            }
        }
        getCorrectDate(way);
        deleteWay(way, 7);

        return way;
    }

    private List<Train> getTrainsCity(City city){
        List<Train> trains = new ArrayList<>();
        for (TrainWay trainWay : city.getTrainWays()) {
            trains.add(trainWay.getTrain());
        }
        return trains;
    }


    private Way calculateFirstRouts(City sourceCity, City destinationCity, Way mainWays) {
        List<Train> currentCityTrains = getTrainsCity(sourceCity);
        if (currentCityTrains != null) {
            for (Train currentCityTrain : currentCityTrains) {

                List<TrainWay> currentTrainCities = currentCityTrain.getTrainWays();
                int indexCityInWayTrain = 0;
                for (int i = 0; i < currentTrainCities.size(); i++) {
                    TrainWay currentTrainWay = currentTrainCities.get(i);

                    if (currentTrainWay.getCity().equals(sourceCity)) {
                        indexCityInWayTrain = i;
                        break;
                    }
                }
                Way currentWay = new Way();
                if (currentTrainCities.size() - 1 > indexCityInWayTrain) {
                    if (currentTrainCities.get(indexCityInWayTrain + 1).getCity().equals(destinationCity)) {
                        currentWay.setFinishWay(true);
                    }
                    currentWay.setPeresadka(0);
                    currentWay.setCoast(currentTrainCities.get(indexCityInWayTrain).getCost());
                    currentWay.setVisitedCities(new ArrayList<>());
                    currentWay.getVisitedCities().add(sourceCity);
                    currentWay.getVisitedCities().add(currentTrainCities.get(indexCityInWayTrain + 1).getCity());
                    currentWay.setWays(new ArrayList<>());
                    List<Way> currentWays = currentWay.getWays();
                    LocalTime currentCityTrainOtpr = currentTrainCities.get(indexCityInWayTrain).getTrainDeparture();
                    LocalTime neighborCityTrainOtpr = currentTrainCities.get(indexCityInWayTrain + 1).getTrainDeparture();
                    currentWays.add(
                            new Way(currentCityTrain, sourceCity,
                                    dateTime.withHour(currentCityTrainOtpr.getHour()).withMinute(currentCityTrainOtpr.getMinute()),
                                    currentTrainCities.get(indexCityInWayTrain).getTrainStoppingTime(),
                                    0));
                    currentWays.add(
                            new Way(currentCityTrain, currentTrainCities.get(indexCityInWayTrain + 1).getCity(),
                                    dateTime.withHour(neighborCityTrainOtpr.getHour()).withMinute(currentCityTrainOtpr.getMinute()),
                                    currentTrainCities.get(indexCityInWayTrain + 1).getTrainStoppingTime(),
                                    0));
                    mainWays.getWays().add(currentWay);
                }


            }
        } else {
            return new Way();
        }
        return mainWays;
    }

    private boolean isFinished(Way mainWays) {
        for (Way way : mainWays.getWays()) {
            if (!way.isFinishWay()) {
                return false;
            }
        }
        mainWays.setFinishWay(true);
        return true;
    }


    private Way calculateWay(City destinationCity, Way mainWays, int per) throws IOException, ClassNotFoundException {
        Set<Way> deleteWays = new LinkedHashSet<>();
        List<Way> newWays = new ArrayList<>();
        List<Way> ways = mainWays.getWays();
        for (int i = 0; i < ways.size(); i++) {
            Way currentWays = ways.get(i);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream ous = new ObjectOutputStream(baos);
            //сохраняем состояние кота Васьки в поток и закрываем его(поток)
            ous.writeObject(currentWays);
            ous.close();
            if (!currentWays.isFinishWay()) {
                List<Way> currentMapWay = currentWays.getWays();
                City currentCity = currentMapWay.get(currentMapWay.size() - 1).getCity();
                List<Train> currentCityTrains = getTrainsCity(currentCity);
                int countTrains = 0;
                if (currentCityTrains != null) {
                    for (int j = 0; j < currentCityTrains.size(); j++) {
                        int peresadka = 0;
                        Train currentTrain = currentCityTrains.get(j);

                        int indexCityInWayTrain = 0;
                        for (int k = 0; k < currentTrain.getTrainWays().size(); k++) {
                            TrainWay currentTrainWay = currentTrain.getTrainWays().get(k);

                            if (currentTrainWay.getCity().equals(currentCity)) {
                                indexCityInWayTrain = k;
                                break;
                            }
                        }


                        if (indexCityInWayTrain != currentTrain.getTrainWays().size() - 1) {
                            City neighborCity = currentTrain.getTrainWays().get(indexCityInWayTrain + 1).getCity();

                            if (currentWays.getVisitedCities().indexOf(neighborCity) != -1) {
                                countTrains++;

                                if (j > currentCityTrains.size() - 2) {
                                    if (countTrains == currentCityTrains.size()) {
                                        deleteWays.add(ways.get(i));
                                    }

                                }
                                continue;
                            }
                            if (!currentWays.getWays().get(currentWays.getWays().size() - 1).getTrain().equals(currentTrain)) {
                                peresadka++;
                            }
                            if (ways.get(i).getPeresadka() + peresadka > per) {
                                countTrains++;
                            } else {

                                ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                                ObjectInputStream ois = new ObjectInputStream(bais);
                                //создаём кота для опытов и инициализируем его состояние Васькиным
                                Way way = (Way) ois.readObject();
                                LocalTime currentCityTrainOtpr = currentTrain.getTrainWays().get(indexCityInWayTrain).getTrainDeparture();
                                LocalTime neighborCityTrainOtpr = currentTrain.getTrainWays().get(indexCityInWayTrain + 1).getTrainStoppingTime();
                                int lastElementIndex = way.getWays().size() - 1;
//                                LocalDateTime localDateTime = null;
//
                                LocalDateTime localDateTime = LocalDateTime.of(
                                        way.getWays().get(lastElementIndex).getOtprTime().getYear(),
                                        way.getWays().get(lastElementIndex).getOtprTime().getMonthValue(),
                                        way.getWays().get(lastElementIndex).getOtprTime().getDayOfMonth(),
                                        neighborCityTrainOtpr.getHour(),
                                        neighborCityTrainOtpr.getMinute());
                                if (peresadka > 0) {

                                    way.setCoast(currentWays.getCoast() + currentTrain.getTrainWays().get(indexCityInWayTrain).getCost());
                                    way.setPeresadka(currentWays.getPeresadka() + 1);
                                    Way lastWayInWays = currentWays.getWays().get(currentWays.getWays().size() - 1);
                                    way.getVisitedCities().add(neighborCity);


                                    way.getWays().add(new Way(currentTrain, lastWayInWays.getCity(),

                                            LocalDateTime.of(
                                                    way.getWays().get(lastElementIndex).getOtprTime().getYear(),
                                                    way.getWays().get(lastElementIndex).getOtprTime().getMonthValue(),
                                                    way.getWays().get(lastElementIndex).getOtprTime().getDayOfMonth(),
                                                    currentCityTrainOtpr.getHour(),
                                                    currentCityTrainOtpr.getMinute()),
                                            currentTrain.getTrainWays().get(indexCityInWayTrain).getTrainStoppingTime(), 1));


                                    way.getWays().add(new Way(currentTrain, neighborCity,

                                            localDateTime,
                                            currentTrain.getTrainWays().get(indexCityInWayTrain + 1).getTrainStoppingTime(), 0));
                                } else {
                                    way.setCoast(currentWays.getCoast() + currentTrain.getTrainWays().get(indexCityInWayTrain).getCost());
                                    way.getWays().add(new Way(currentTrain, neighborCity,
                                            localDateTime,
                                            currentTrain.getTrainWays().get(indexCityInWayTrain + 1).getTrainStoppingTime(), 0));
                                    way.getVisitedCities().add(neighborCity);
                                }

                                deleteWays.add(ways.get(i));
                                if (way.getWays().get(way.getWays().size() - 1).getCity().equals(destinationCity)) {
                                    way.setFinishWay(true);
                                }
                                newWays.add(way);
                            }
                            if (countTrains == currentCityTrains.size()) {
                                ways.remove(currentWays);
                            }


                        }
                        else {
                            deleteWays.add(ways.get(i));
                        }

                    }
                } else {
                    deleteWays.add(ways.get(i));
                }
            }
        }
        mainWays.getWays().addAll(newWays);
        mainWays.getWays().removeAll(deleteWays);
        return mainWays;
    }

    public void getCorrectDate(Way mainWays) {
        if (mainWays.getWays() != null)
            for (int i = 0; i < mainWays.getWays().size(); i++) {
                for (int j = 0; j < mainWays.getWays().get(i).getWays().size() - 1; j++) {
                    if (mainWays.getWays().get(i).getWays().get(j).getOtprTime().toLocalTime().toSecondOfDay() > mainWays.getWays().get(i).getWays().get(j + 1).getOtprTime().toLocalTime().toSecondOfDay()) {
                        mainWays.getWays().get(i).getWays().get(j + 1).setOtprTime(mainWays.getWays().get(i).getWays().get(j + 1).getOtprTime().plusDays(1));
                        for (int k = 1; k < mainWays.getWays().get(i).getWays().size() - 1 - j; k++) {
                            mainWays.getWays().get(i).getWays().get(k + j + 1).setOtprTime(mainWays.getWays().get(i).getWays().get(k + j + 1).getOtprTime().plusDays(1));
                        }
                    }
                }
            }
    }

    public void deleteWay(Way mainWays, int timeOzidaniy) {
        List<Way> deleteObjects = new ArrayList<>();
        List<Integer> k = new ArrayList<>();
        if (mainWays.getWays() != null) {
            for (int i = 0; i < mainWays.getWays().size(); i++) {
                for (int j = 0; j < mainWays.getWays().get(i).getWays().size() - 2; j++) {
                    if (!mainWays.getWays().get(i).getWays().get(j).getTrain().getName().equals(mainWays.getWays().get(i).getWays().get(j + 1).getTrain().getName())) {
                        if (mainWays.getWays().get(i).getWays().get(j).getOtprTime().getDayOfMonth() < mainWays.getWays().get(i).getWays().get(j + 1).getOtprTime().getDayOfMonth()) {
//                        deleteObjects.add(mainWays.getWays().get(i));
                            if (mainWays.getWays().get(i).getWays().get(j).getOtprTime().plusHours(timeOzidaniy).getDayOfMonth()
                                    != mainWays.getWays().get(i).getWays().get(j + 1).getOtprTime().getDayOfMonth()) {
                                deleteObjects.add(mainWays.getWays().get(i));

                            } else {
                                if (mainWays.getWays().get(i).getWays().get(j).getOtprTime().plusHours(timeOzidaniy).toLocalTime().toSecondOfDay()
                                        <= mainWays.getWays().get(i).getWays().get(j + 1).getOtprTime().toLocalTime().toSecondOfDay()) {
                                    deleteObjects.add(mainWays.getWays().get(i));
                                }
                            }
                        } else {
                            int maxTime;
                            if (LocalTime.of(23, 59).toSecondOfDay()
                                    - mainWays.getWays().get(i).getWays().get(j).getOtprTime().plusMinutes(1).toLocalTime().toSecondOfDay()
                                    >= LocalTime.of(timeOzidaniy, 0).toSecondOfDay()) {
                                maxTime = mainWays.getWays().get(i).getWays().get(j).getOtprTime().plusMinutes(1).plusHours(timeOzidaniy).toLocalTime().toSecondOfDay();
                            } else {
                                maxTime = LocalTime.of(23, 59).toSecondOfDay();
                            }

                            if (mainWays.getWays().get(i).getWays().get(j).getOtprTime().minusMinutes(1).toLocalTime().toSecondOfDay()
                                    >= mainWays.getWays().get(i).getWays().get(j + 1).getOtprTime().toLocalTime().toSecondOfDay()
                                    || mainWays.getWays().get(i).getWays().get(j + 1).getOtprTime().toLocalTime().toSecondOfDay()
                                    >= maxTime) {
                                deleteObjects.add(mainWays.getWays().get(i));

                            }
                        }
                    }
                }
            }
            mainWays.getWays().removeAll(deleteObjects);
        }
    }

    public LocalTime getLocalTime(int time){
        int hour = time/3600;
        int minute = (time-hour)/60;
        LocalTime localTime = LocalTime.now().withHour(hour).withMinute(minute).withSecond(0).withNano(0);
        return localTime;

    }

    public void printWays(Way way) {
        int count = 1;
        if (way != null) {
            List<Way> ways = way.getWays();
            if (ways != null) {

                for (Way way1 : ways) {
                    System.out.print(count + " finish = " + way1.isFinishWay() + " per = " + way1.getPeresadka());
                    System.out.print(" cities " + way1.getVisitedCities().size() + " =");
                    for (City city : way1.getVisitedCities()) {
                        System.out.print(city.getName() + ",");
                    }

                    System.out.print(" coast = " + way1.getCoast());
                    System.out.println();
                    for (Way way2 : way1.getWays()) {
                        System.out.println(way2.getPeresadka() + "." + way2.getTrain().getName() + "." + way2.getCity().getName() + ".[" + way2.getOtprTime().format(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm:ss")) + "][" + way2.getStopTime().format(DateTimeFormatter.ofPattern("HH:mm")) + "] ->");
                    }
                    System.out.println();
                    count++;
//                    System.out.println("co  = " + count);
                }
            }
        } else {
            System.out.println("No");
        }
    }

}




