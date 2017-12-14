package com.vladimir.zubencko;

import com.vladimir.zubencko.domain.Station;
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

    private boolean validateStation(Station startStation, Station finishStation) {
        if (startStation.equals(finishStation)) {
            System.out.println("Source station and destination neighbor station is equal");
            return false;
        }
        return true;
    }

    public Way getWay(Station sourceStation, Station destinationStation, int transplant, LocalDateTime sourceDate,LocalDateTime destinationDate, int waitingTime) throws IOException, ClassNotFoundException {
        Way way = new Way();
        if (validateStation(sourceStation, destinationStation)) {
            way.setWays(new ArrayList<>());
            getFirstRouts(sourceStation, destinationStation, way, sourceDate);
            while (!isFinished(way)) {
                getNextWay(destinationStation, way, transplant);
                if (way.getWays().size() == 0) {
                    return null;
                }
            }
            if (way.getWays().size() == 0) {
                return null;
            }
        } else {
            return null;
        }
        getCorrectDate(way);
        deleteWay(way, waitingTime);
        deleteWayNotSuitableArrivalTime(way, destinationDate);

        return way;
    }

    private void deleteWayNotSuitableArrivalTime(Way way, LocalDateTime destinationDate){
        List<Way> deletedWays = new ArrayList<>();
        for (Way way1 : way.getWays()) {
            Way lastWay = way1.getWays().get(way1.getWays().size() - 1);
            if (lastWay.getDepartureTime().toLocalDate().toEpochDay()>destinationDate.toLocalDate().toEpochDay()){
                deletedWays.add(way1);
            }else{
                if (lastWay.getDepartureTime().toLocalDate().toEpochDay()==destinationDate.toLocalDate().toEpochDay()){
                    LocalTime time = lastWay.getDepartureTime().toLocalTime();
                    if (time.minusHours(lastWay.getStoppingTime().getHour()).
                            minusMinutes(lastWay.getStoppingTime().getMinute()).toSecondOfDay()
                            >destinationDate.toLocalTime().toSecondOfDay()){
                        deletedWays.add(way1);
                    }
                }
            }
        }
        for (Way deletedWay : deletedWays) {
            way.getWays().remove(deletedWay);
        }
    }

    private List<Train> getTrainsCity(Station station) {
        List<Train> trains = new ArrayList<>();
        for (TrainWay trainWay : station.getTrainWays()) {
            trains.add(trainWay.getTrain());
        }
        return trains;
    }

    private Way getFirstRouts(Station sourceStation, Station destinationStation, Way mainWays, LocalDateTime sourceDate) {
        List<Train> currentCityTrains = getTrainsCity(sourceStation);
        if (currentCityTrains != null) {
            for (Train currentCityTrain : currentCityTrains) {

                List<TrainWay> currentTrainCities = currentCityTrain.getTrainWays();
                int indexCityInWayTrain = 0;
                TrainWay currentTrainWay = null;
                for (int i = 0; i < currentTrainCities.size(); i++) {
                     currentTrainWay = currentTrainCities.get(i);
                    if (currentTrainWay.getStation().equals(sourceStation)) {
                        indexCityInWayTrain = i;
                        break;
                    }
                }
                if (currentTrainWay!=null){
                    if (currentTrainWay.getTrainDeparture().toSecondOfDay()>=sourceDate.toLocalTime().toSecondOfDay()){
                        saveFirstWay(currentTrainCities, indexCityInWayTrain, currentCityTrain, mainWays, sourceStation, destinationStation, sourceDate);
                    }
                }

            }
        } else {
            return new Way();
        }
        return mainWays;
    }

    private void saveFirstWay(List<TrainWay> currentTrainCities, int indexCityInWayTrain, Train currentCityTrain,
                              Way mainWays, Station sourceStation, Station destinationStation, LocalDateTime sourceDate) {
        Way currentWay = new Way();
        if (currentTrainCities.size() - 1 > indexCityInWayTrain) {
            if (currentTrainCities.get(indexCityInWayTrain + 1).getStation().equals(destinationStation)) {
                currentWay.setFinishWay(true);
            }
            currentWay.setTransplant(0);
            currentWay.setCost(currentTrainCities.get(indexCityInWayTrain).getCost());
            currentWay.setVisitedCities(new ArrayList<>());
            currentWay.getVisitedCities().add(sourceStation);
            currentWay.getVisitedCities().add(currentTrainCities.get(indexCityInWayTrain + 1).getStation());
            currentWay.setWays(new ArrayList<>());
            List<Way> currentWays = currentWay.getWays();
            LocalTime currentCityTrainDeparture = getTime(currentTrainCities, indexCityInWayTrain);
            LocalTime neighborCityTrainDeparture = getTime(currentTrainCities, indexCityInWayTrain + 1);
            Way waySourceCity = getCurrentWay(currentCityTrain, sourceStation, currentCityTrainDeparture,
                    currentTrainCities, indexCityInWayTrain, sourceDate);
            Way wayNextCity = getCurrentWay(currentCityTrain, currentTrainCities.get(indexCityInWayTrain + 1).getStation(),
                    neighborCityTrainDeparture, currentTrainCities, indexCityInWayTrain + 1, sourceDate);
            currentWays.add(waySourceCity);
            currentWays.add(wayNextCity);
            mainWays.getWays().add(currentWay);
        }
    }

    private LocalTime getTime(List<TrainWay> currentTrainCities, int indexCityInWayTrain) {
        return currentTrainCities.get(indexCityInWayTrain).getTrainDeparture();
    }

    private Way getCurrentWay(Train currentCityTrain, Station sourceStation, LocalTime TrainDeparture,
                              List<TrainWay> currentTrainCities, int indexCityInWayTrain, LocalDateTime dateTime) {
        return new Way(currentCityTrain, sourceStation,
                dateTime.withHour(TrainDeparture.getHour()).withMinute(TrainDeparture.getMinute()),
                currentTrainCities.get(indexCityInWayTrain).getTrainStoppingTime(),
                0);
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


    private void getNextWay(Station destinationStation, Way mainWays, int transplant) throws IOException, ClassNotFoundException {
        Set<Way> deletedWays = new LinkedHashSet<>();
        List<Way> newWays = new ArrayList<>();
        List<Way> ways = mainWays.getWays();
        for (int i = 0; i < ways.size(); i++) {
            Way currentWays = ways.get(i);
            ByteArrayOutputStream baos = writeObject(currentWays);
            if (!currentWays.isFinishWay()) {
                List<Way> currentMapWay = currentWays.getWays();
                Station currentStation = currentMapWay.get(currentMapWay.size() - 1).getStation();
                List<Train> currentCityTrains = getTrainsCity(currentStation);
                int countTrains = 0;
                if (currentCityTrains != null) {
                    for (int j = 0; j < currentCityTrains.size(); j++) {
                        int transplantCount = 0;
                        Train currentTrain = currentCityTrains.get(j);
                        int indexCityInWayTrain = 0;
                        for (int k = 0; k < currentTrain.getTrainWays().size(); k++) {
                            TrainWay currentTrainWay = currentTrain.getTrainWays().get(k);
                            if (currentTrainWay.getStation().equals(currentStation)) {
                                indexCityInWayTrain = k;
                                break;
                            }
                        }
                        if (indexCityInWayTrain != currentTrain.getTrainWays().size() - 1) {
                            Station neighborStation = currentTrain.getTrainWays().get(indexCityInWayTrain + 1).getStation();
                            if (currentWays.getVisitedCities().indexOf(neighborStation) != -1) {
                                countTrains++;
                                if (j > currentCityTrains.size() - 2) {
                                    if (countTrains == currentCityTrains.size()) {
                                        deletedWays.add(ways.get(i));
                                    }
                                }
                                continue;
                            }
                            Way way = readObject(baos);
                            checkTransplant(currentWays, currentTrain, transplantCount, ways, i, countTrains,
                                    indexCityInWayTrain, way, neighborStation, deletedWays, newWays, currentCityTrains,
                                    destinationStation, transplant);

                        } else {
                            deletedWays.add(ways.get(i));
                        }

                    }
                } else {
                    deletedWays.add(ways.get(i));
                }
            }
        }
        mainWays.getWays().addAll(newWays);
        mainWays.getWays().removeAll(deletedWays);
    }

    private void checkTransplant(Way currentWays, Train currentTrain, int transplantCount, List<Way> ways,
                                 int i, int countTrains, int indexCityInWayTrain, Way way, Station neighborStation,
                                 Set<Way> deletedWays, List<Way> newWays, List<Train> currentCityTrains,
                                 Station destinationStation, int transplant) {
        if (!currentWays.getWays().get(currentWays.getWays().size() - 1).getTrain().equals(currentTrain)) {
            transplantCount++;
        }
        if (ways.get(i).getTransplant() + transplantCount > transplant) {
            countTrains++;
        } else {

            LocalTime currentCityTrainDeparture = getTime
                    (currentTrain.getTrainWays(), indexCityInWayTrain);
            LocalTime neighborCityTrainDeparture = getTime
                    (currentTrain.getTrainWays(), indexCityInWayTrain + 1);
            int lastElementIndex = way.getWays().size() - 1;
            LocalDateTime localDateTime = getLocalDataTime
                    (way, lastElementIndex, neighborCityTrainDeparture);
            setWay(transplantCount, way, currentWays, currentTrain,
                    indexCityInWayTrain, neighborStation, lastElementIndex,
                    currentCityTrainDeparture, localDateTime);
            deletedWays.add(ways.get(i));
            if (way.getWays().get(way.getWays().size() - 1).getStation().equals(destinationStation)) {
                way.setFinishWay(true);
            }
            newWays.add(way);
        }
        if (countTrains == currentCityTrains.size()) {
            ways.remove(currentWays);
        }
    }


    private LocalDateTime getLocalDataTime(Way way, int lastElementIndex, LocalTime neighborCityTrainDeparture) {
        return LocalDateTime.of(
                way.getWays().get(lastElementIndex).getDepartureTime().getYear(),
                way.getWays().get(lastElementIndex).getDepartureTime().getMonthValue(),
                way.getWays().get(lastElementIndex).getDepartureTime().getDayOfMonth(),
                neighborCityTrainDeparture.getHour(),
                neighborCityTrainDeparture.getMinute());
    }

    private void setWay(int transplantCount, Way way, Way currentWays, Train currentTrain,
                        int indexCityInWayTrain, Station neighborStation, int lastElementIndex,
                        LocalTime currentCityTrainDeparture, LocalDateTime localDateTime) {
        if (transplantCount > 0) {
            way.setCost(currentWays.getCost() + currentTrain.getTrainWays().get(indexCityInWayTrain).getCost());
            way.setTransplant(currentWays.getTransplant() + 1);
            Way lastWayInWays = currentWays.getWays().get(currentWays.getWays().size() - 1);
            way.getVisitedCities().add(neighborStation);
            way.getWays().add(new Way(currentTrain, lastWayInWays.getStation(),
                    LocalDateTime.of(
                            way.getWays().get(lastElementIndex).getDepartureTime().getYear(),
                            way.getWays().get(lastElementIndex).getDepartureTime().getMonthValue(),
                            way.getWays().get(lastElementIndex).getDepartureTime().getDayOfMonth(),
                            currentCityTrainDeparture.getHour(),
                            currentCityTrainDeparture.getMinute()),
                    currentTrain.getTrainWays().get(indexCityInWayTrain).getTrainStoppingTime(), 1));
            way.getWays().add(new Way(currentTrain, neighborStation,

                    localDateTime,
                    currentTrain.getTrainWays().get(indexCityInWayTrain + 1).getTrainStoppingTime(), 0));
        } else {
            way.setCost(currentWays.getCost() + currentTrain.getTrainWays().get(indexCityInWayTrain).getCost());
            way.getWays().add(new Way(currentTrain, neighborStation,
                    localDateTime,
                    currentTrain.getTrainWays().get(indexCityInWayTrain + 1).getTrainStoppingTime(), 0));
            way.getVisitedCities().add(neighborStation);
        }
    }

    private ByteArrayOutputStream writeObject(Way currentWays) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream ous = new ObjectOutputStream(baos);
        ous.writeObject(currentWays);
        ous.close();
        return baos;
    }

    private Way readObject(ByteArrayOutputStream baos) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (Way) ois.readObject();
    }

    private void getCorrectDate(Way mainWays) {
        List<Way> ways = mainWays.getWays();
        if (ways != null)
            for (Way way : ways) {
                List<Way> currentWayWays = way.getWays();
                for (int j = 0; j < currentWayWays.size() - 1; j++) {
                    Way currentTrainWay = currentWayWays.get(j);
                    Way nextTrainWay = currentWayWays.get(j + 1);
                    int currentTrainDepartureToSecond = getSecondsOfDay(currentTrainWay);
                    int nextTrainDepartureToSecond = getSecondsOfDay(nextTrainWay);
                    if (currentTrainDepartureToSecond > nextTrainDepartureToSecond) {
                        addOneDayToNextWays(nextTrainWay, currentWayWays, j);
                    }
                }
            }
    }

    private void addOneDayToNextWays(Way nextTrainWay, List<Way> currentWayWays, int currentWayWaysIndex) {
        nextTrainWay.setDepartureTime(nextTrainWay.getDepartureTime().plusDays(1));
        for (int k = 1; k < currentWayWays.size() - 1 - currentWayWaysIndex; k++) {
            Way nextWayForNextWay = currentWayWays.get(k + currentWayWaysIndex + 1);
            nextWayForNextWay.setDepartureTime(nextWayForNextWay.getDepartureTime().plusDays(1));
        }
    }

    private int getSecondsOfDay(Way way) {
        return way.getDepartureTime().toLocalTime().toSecondOfDay();
    }

    private void deleteWay(Way mainWays, int waitingTimeTrain) {
        List<Way> deleteObjects = new ArrayList<>();
        List<Way> mainWayWays = mainWays.getWays();
        if (mainWays.getWays() != null) {
            for (int i = 0; i < mainWays.getWays().size(); i++) {
                List<Way> ways = mainWays.getWays().get(i).getWays();
                for (int j = 0; j < ways.size() - 2; j++) {
                    deleteWayNonQualifyingCondition(ways, j, i, mainWayWays, waitingTimeTrain, deleteObjects);
                }
            }
            mainWays.getWays().removeAll(deleteObjects);
        }
    }

    private void deleteWayNonQualifyingCondition(List<Way> ways, int indexWatOnTrainWay, int indexCurrentWay,
                                                 List<Way> mainWayWays, int waitingTimeTrain, List<Way> deleteObjects) {
        Way currentTrainWay = ways.get(indexWatOnTrainWay);
        Way nextTrainWay = ways.get(indexWatOnTrainWay + 1);
        Way currentWay = mainWayWays.get(indexCurrentWay);
        if (!currentTrainWay.getTrain().getName().equals(nextTrainWay.getTrain().getName())) {
            LocalDateTime currentTrainWayDeparture = currentTrainWay.getDepartureTime();
            LocalDateTime nextTrainWayDeparture = nextTrainWay.getDepartureTime();
            if (currentTrainWayDeparture.getDayOfMonth() < nextTrainWayDeparture.getDayOfMonth()) {
                checkDate(currentTrainWayDeparture, waitingTimeTrain, nextTrainWayDeparture, deleteObjects, currentWay);
            } else {
                checkTime(currentTrainWayDeparture, waitingTimeTrain, nextTrainWayDeparture, deleteObjects,
                        currentWay, currentTrainWay);
            }
        }
    }

    private void checkTime(LocalDateTime currentTrainWayDeparture, int waitingTimeTrain,
                           LocalDateTime nextTrainWayDeparture, List<Way> deleteObjects,
                           Way currentWay, Way currentTrainWay) {
        int maxTime;
        int time = LocalTime.of(23, 59).toSecondOfDay();
        int currentTrainWayStoppingHour = currentTrainWay.getStoppingTime().getHour();
        int currentTrainWayStoppingMinute = currentTrainWay.getStoppingTime().getMinute();
        LocalDateTime time2 = currentTrainWayDeparture.plusMinutes(1).minusHours(currentTrainWayStoppingHour).
                minusMinutes(currentTrainWayStoppingMinute);
        LocalDateTime maxTimeDepartureNextTrain = currentTrainWayDeparture.plusMinutes(1).plusHours(waitingTimeTrain).
                minusHours(currentTrainWayStoppingHour).minusMinutes(currentTrainWayStoppingMinute);
        if (time - getSecondsOfDay(time2)
                >= LocalTime.of(waitingTimeTrain, 0).toSecondOfDay()) {
            maxTime = getSecondsOfDay(maxTimeDepartureNextTrain);
        } else {
            maxTime = time;
        }
        if (getSecondsOfDay(currentTrainWayDeparture.minusHours(currentTrainWayStoppingHour).minusMinutes(currentTrainWayStoppingMinute + 1))
                >= getSecondsOfDay(nextTrainWayDeparture)
                || getSecondsOfDay(nextTrainWayDeparture)
                >= maxTime) {
            deleteObjects.add(currentWay);
        }
    }

    private void checkDate(LocalDateTime currentTrainWayDeparture, int waitingTimeTrain,
                           LocalDateTime nextTrainWayDeparture, List<Way> deleteObjects, Way currentWay) {
        if (currentTrainWayDeparture.plusHours(waitingTimeTrain).getDayOfMonth()
                != nextTrainWayDeparture.getDayOfMonth()) {
            deleteObjects.add(currentWay);

        } else {
            if (getSecondsOfDay(currentTrainWayDeparture.plusHours(waitingTimeTrain))
                    <= getSecondsOfDay(nextTrainWayDeparture)) {
                deleteObjects.add(currentWay);
            }
        }
    }

    private int getSecondsOfDay(LocalDateTime time) {
        return time.toLocalTime().toSecondOfDay();
    }

    public void printWays(Way way) {
        int count = 1;
        if (way != null&&!way.getWays().isEmpty()) {
            List<Way> ways = way.getWays();
            if (ways != null) {

                for (Way way1 : ways) {
                    System.out.print(count + " finish = " + way1.isFinishWay() + " per = " + way1.getTransplant());
                    System.out.print(" cities " + way1.getVisitedCities().size() + " =");
                    for (Station station : way1.getVisitedCities()) {
                        System.out.print(station.getName() + ",");
                    }
                    System.out.print(" cost = " + way1.getCost());
                    System.out.println();
                    for (Way way2 : way1.getWays()) {
                        System.out.println(way2.getTransplant() + "." + way2.getTrain().getName() + "." + way2.getStation().getName() + ".[" + way2.getDepartureTime().format(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm:ss")) + "][" + way2.getStoppingTime().format(DateTimeFormatter.ofPattern("HH:mm")) + "] ->");
                    }
                    System.out.println();
                    count++;
                }
            }
        } else {
            System.out.println("No");
        }
    }

}




