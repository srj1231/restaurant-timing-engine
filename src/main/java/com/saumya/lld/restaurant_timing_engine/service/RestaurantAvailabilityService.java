package com.saumya.lld.restaurant_timing_engine.service;

import com.saumya.lld.restaurant_timing_engine.entities.OpeningSchedule;
import com.saumya.lld.restaurant_timing_engine.entities.Restaurant;
import com.saumya.lld.restaurant_timing_engine.entities.TimeSlot;
import com.saumya.lld.restaurant_timing_engine.entities.availability.AvailabilityIndex;
import com.saumya.lld.restaurant_timing_engine.repository.OpeningHoursRepository;
import com.saumya.lld.restaurant_timing_engine.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@FieldDefaults(makeFinal = true)
public class RestaurantAvailabilityService {

    RestaurantRepository restaurantRepository;
    OpeningHoursRepository openingHoursRepository;
    AvailabilityIndex availabilityIndex;

    // read 1: get a restaurant's opening hours
    public List<TimeSlot> getOpeningHours(String restaurantId) {
        OpeningSchedule openingSchedule = openingHoursRepository.get(restaurantId);

        if(openingSchedule == null){
            throw new IllegalArgumentException("Schedule not found for restaurant " + restaurantId);
        }

        return openingSchedule.getSlots();
    }

    // read 2: get all restaurants open at a given time
    public List<Restaurant> getOpenRestaurants(Instant instant) {
        Set<String> ids = availabilityIndex.getOpenRestaurants(instant, restaurantRepository);
        List<Restaurant> openRestaurants = new ArrayList<>();

        for(String id : ids){
            Restaurant restaurant = restaurantRepository.get(id);

            if(restaurant != null) openRestaurants.add(restaurant);
        }

        return openRestaurants;
    }

    // write: update opening hours
    public void updateOpeningHours(String restaurantId, List<TimeSlot> slots) {
        Restaurant restaurant = restaurantRepository.get(restaurantId);

        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant does not exist");
        }

        OpeningSchedule schedule = new OpeningSchedule(restaurantId, slots);

        openingHoursRepository.save(schedule); // source of truth

        availabilityIndex.update(restaurant, schedule); // Read optimized index
    }
}
