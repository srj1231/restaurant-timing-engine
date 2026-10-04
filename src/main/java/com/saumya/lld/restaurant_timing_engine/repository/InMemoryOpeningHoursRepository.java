package com.saumya.lld.restaurant_timing_engine.repository;

import com.saumya.lld.restaurant_timing_engine.entities.OpeningSchedule;
import com.saumya.lld.restaurant_timing_engine.entities.Restaurant;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryOpeningHoursRepository implements OpeningHoursRepository {

    private final Map<String, OpeningSchedule> openingHours = new ConcurrentHashMap<>();

    @Override
    public OpeningSchedule get(String restaurantId) {
        return openingHours.get(restaurantId);
    }

    @Override
    public void save(OpeningSchedule openingSchedule) {
        openingHours.put(openingSchedule.getRestaurantId(), openingSchedule);
    }

    @Override
    public void delete(String restaurantId) {
        openingHours.remove(restaurantId);
    }
}
