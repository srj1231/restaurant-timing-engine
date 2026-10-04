package com.saumya.lld.restaurant_timing_engine.entities.availability;

import com.saumya.lld.restaurant_timing_engine.entities.schedule.OpeningSchedule;
import com.saumya.lld.restaurant_timing_engine.entities.Restaurant;
import com.saumya.lld.restaurant_timing_engine.repository.RestaurantRepository;

import java.time.Instant;
import java.util.Set;

public interface AvailabilityIndex {

    Set<String> getOpenRestaurants(Instant instant, RestaurantRepository restaurantRepository);

    void update(Restaurant restaurant, OpeningSchedule openingSchedule);

    void removeRestaurant(Restaurant restaurant);
}
