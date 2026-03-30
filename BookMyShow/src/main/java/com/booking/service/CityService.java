package com.booking.service;

import com.booking.exception.NotFoundException;
import com.booking.model.City;
import com.booking.model.Theater;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class CityService {

    private static volatile CityService instance;
    private final Map<String, City> cities = new ConcurrentHashMap<>();

    private CityService() {}

    public static CityService getInstance() {
        if (instance == null) {
            synchronized (CityService.class) {
                if (instance == null) instance = new CityService();
            }
        }
        return instance;
    }

    public void addCity(City city) {
        cities.put(city.getId(), city);
    }

    public City getCity(String cityId) {
        City city = cities.get(cityId);
        if (city == null) throw new NotFoundException("City not found: " + cityId);
        return city;
    }

    public City getCityByName(String name) {
        return cities.values().stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("City not found: " + name));
    }

    /** Returns all theaters in a city — the showTheaters API entry point. */
    public List<Theater> getTheatersInCity(String cityId) {
        return getCity(cityId).getTheaters();
    }

    public List<City> getAllCities() {
        return new ArrayList<>(cities.values());
    }
}
