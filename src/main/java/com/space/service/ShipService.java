package com.space.service;

import com.space.controller.ShipOrder;
import com.space.exceptions.BadRequestException;
import com.space.exceptions.NotFoundException;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShipService {
    private final ShipRepository repository;

    public ShipService(ShipRepository repository) {
        this.repository = repository;
    }

    public List<Ship> getShipsList(String name, String planet,
                                   ShipType shipType, Long after, Long before,
                                   Boolean isUsed,
                                   Double minSpeed, Double maxSpeed,
                                   Integer minCrewSize, Integer maxCrewSize,
                                   Double minRating, Double maxRating,
                                   ShipOrder order,
                                   Integer pageNumber, Integer pageSize) {
        if (pageNumber ==null) pageNumber = 0;
        if (pageSize == null) pageSize = 3;

        String myOrder = ShipOrder.ID.getFieldName();
        if (order != null){
            myOrder = order.getFieldName();
        }
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(myOrder));

        return repository.findParameters(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating,maxRating, pageRequest).getContent();
    }


    public Integer getShipsCount(String name, String planet,
                                 ShipType shipType, Long after, Long before,
                                 Boolean isUsed,
                                 Double minSpeed, Double maxSpeed,
                                 Integer minCrewSize, Integer maxCrewSize,
                                 Double minRating, Double maxRating) {
        return 0;
    }

    public void deleteShip(Long id) {
        checkID(id);
        repository.deleteById(id);
    }

    private void checkID(Long id) {
        if (id == null || id <= 0)
            throw new BadRequestException();

        if (!hasShip(id))
            throw new NotFoundException();
    }

    private boolean hasShip(Long id) {
        return repository.existsById(id);
    }

    public Ship getShipById(Long id) {
        checkID(id);
        return repository.findById(id).orElseThrow(IllegalStateException::new);
    }

    public Ship updateShip(Long id, String name, String planet, ShipType shipType, Long prodDate, Boolean isUsed, Double speed, Integer crewSize) {
        checkID(id);

        Ship ship = repository.findById(id).orElseThrow(IllegalStateException::new);


        if (name != null) {
            checkStringValue(name);
            ship.setName(name);
        }
        if (planet != null) {
            checkStringValue(planet);
            ship.setPlanet(planet);
        }
        if (shipType != null) ship.setShipType(shipType);
        if (prodDate != null) {
            checkProdDate(prodDate);
            ship.setProdDate(new Date(prodDate));
        }
        if (isUsed != null) ship.setUsed(isUsed);
        if (speed != null) {
            checkSpeed(speed);
            ship.setSpeed(speed);
        }
        double rating = ratingCalc(ship.getSpeed(), ship.getUsed(), ship.getProdDate());
        ship.setRating(rating);

        if (crewSize != null) {
            checkCrewSize(crewSize);
            ship.setCrewSize(crewSize);
        }

        return repository.save(ship);
    }


    private int checkDate(Long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return calendar.get(Calendar.YEAR);
    }

    public Ship createShip(String name, String planet, ShipType shipType, Long prodDate, Boolean isUsed, Double speed, Integer crewSize) {

        checkStringValue(name);
        checkStringValue(planet);
        checkProdDate(prodDate);
        checkSpeed(speed);
        checkCrewSize(crewSize);

        if (shipType == null) throw new BadRequestException();
        if (isUsed == null) isUsed = false;

        Ship ship = new Ship();
        ship.setName(name);
        ship.setPlanet(planet);
        ship.setShipType(shipType);
        ship.setProdDate(new Date(prodDate));
        ship.setUsed(isUsed);
        ship.setSpeed(speed);
        ship.setCrewSize(crewSize);
        double rating = ratingCalc(speed, isUsed, ship.getProdDate());
        ship.setRating(rating);

        return repository.save(ship);
    }

    private void checkCrewSize(Integer crewSize) {
        if (crewSize == null) throw new BadRequestException();
        if (crewSize < 1 || crewSize > 9999)
            throw new BadRequestException();
    }

    private void checkSpeed(Double speed) {
        if (speed == null) throw new BadRequestException();
        if (speed < 0.01d || speed > 0.99d) throw new BadRequestException();
    }

    private void checkProdDate(Long prodDate) {
        if (prodDate == null || checkDate(prodDate) < 2800 || checkDate(prodDate) > 3019)
            throw new BadRequestException();
    }

    private void checkStringValue(String value) {
        if (value == null) throw new BadRequestException();
        if (value.length() > 50) throw new BadRequestException();
        if (value.isEmpty()) throw new BadRequestException();
    }

    public Double ratingCalc(Double speed, Boolean isUsed, Date date) {

        double k = isUsed ? 0.5d : 1d;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        double y1 = calendar.get(Calendar.YEAR);

        double result = (80 * speed * k) / (3019 - y1 + 1);
        double scale = Math.pow(10, 2);
        return Math.round(result * scale) / scale;
    }

}
