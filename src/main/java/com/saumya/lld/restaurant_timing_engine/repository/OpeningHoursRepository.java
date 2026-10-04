package com.saumya.lld.restaurant_timing_engine.repository;

import com.saumya.lld.restaurant_timing_engine.entities.OpeningSchedule;

public interface OpeningHoursRepository {

    OpeningSchedule get(String restaurantId);

    void save(OpeningSchedule openingSchedule);

    void delete(String restaurantId);
}
