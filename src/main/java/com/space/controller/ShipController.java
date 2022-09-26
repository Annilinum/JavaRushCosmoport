package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/ships")
public class ShipController {

    @Autowired
    private ShipService shipService;

    @GetMapping()
    public List<Ship> getShipsList(String name, String planet,
                                   ShipType shipType, Long after, Long before,
                                   Boolean isUsed,
                                   Double minSpeed, Double maxSpeed,
                                   Integer minCrewSize, Integer maxCrewSize,
                                   Double minRating, Double maxRating,
                                   ShipOrder order,
                                   Integer pageNumber, Integer pageSize) {
        return shipService.getShipsList(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating, order, pageNumber, pageSize);
    }

    @GetMapping("/count")
    public int getShipsCount(String name, String planet,
                             ShipType shipType, Long after, Long before,
                             Boolean isUsed,
                             Double minSpeed, Double maxSpeed,
                             Integer minCrewSize, Integer maxCrewSize,
                             Double minRating, Double maxRating) {
        return shipService.getShipsCount(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
    }

    @GetMapping("/{id}")
    public Ship getShipById(@PathVariable Long id) {
        return shipService.getShipById(id);
    }

    @PostMapping()
    public Ship createShip(@RequestBody ShipRequest shipRequest) {
        return shipService.createShip(
                shipRequest.getName(),
                shipRequest.getPlanet(),
                shipRequest.getShipType(),
                shipRequest.getProdDate(),
                shipRequest.getUsed(),
                shipRequest.getSpeed(),
                shipRequest.getCrewSize()
        );
    }

    @PostMapping("/{id}")
    public Ship updateShip(@PathVariable Long id, @RequestBody ShipRequest shipRequest) {
        return shipService.updateShip(
                id, shipRequest.getName(),
                shipRequest.getPlanet(),
                shipRequest.getShipType(),
                shipRequest.getProdDate(),
                shipRequest.getUsed(),
                shipRequest.getSpeed(),
                shipRequest.getCrewSize()
        );
    }

    @DeleteMapping("/{id}")
    public void deleteShip(@PathVariable Long id) {
        shipService.deleteShip(id);
    }
}
