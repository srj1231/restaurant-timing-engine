package com.saumya.lld.restaurant_timing_engine.repository;

import com.saumya.lld.restaurant_timing_engine.entities.schedule.OpeningSchedule;
import com.saumya.lld.restaurant_timing_engine.entities.schedule.RestaurantSchedule;

public interface OpeningHoursRepository {

    OpeningSchedule get(String restaurantId);

    void save(OpeningSchedule openingSchedule);

    void delete(String restaurantId);

    RestaurantSchedule getSchedule(String restaurantId);

    void saveSchedule(RestaurantSchedule schedule);
}
