package com.saumya.lld.restaurant_timing_engine.service;

import com.saumya.lld.restaurant_timing_engine.entities.schedule.OpeningSchedule;
import com.saumya.lld.restaurant_timing_engine.entities.Restaurant;
import com.saumya.lld.restaurant_timing_engine.entities.TimeSlot;
import com.saumya.lld.restaurant_timing_engine.entities.availability.AvailabilityIndex;
import com.saumya.lld.restaurant_timing_engine.entities.schedule.RestaurantSchedule;
import com.saumya.lld.restaurant_timing_engine.entities.schedule.ScheduleSnapshot;
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
        RestaurantSchedule schedule = openingHoursRepository.getSchedule(restaurantId);

        if(schedule == null){
            throw new IllegalArgumentException("Schedule not found for restaurant " + restaurantId);
        }

        return schedule.getSnapshot().getSlots();
    }

    // read 2: get all restaurants open at a given time
    public List<Restaurant> getOpenRestaurants(Instant instant) {
        Set<String> ids = availabilityIndex.getOpenRestaurants(instant, restaurantRepository);
        List<Restaurant> openRestaurants = new ArrayList<>();

        for(String id : ids){
            Restaurant restaurant = restaurantRepository.getById(id);

            if(restaurant != null) openRestaurants.add(restaurant);
        }

        return openRestaurants;
    }

    // write: update opening hours with optimistic concurrency
    public void updateOpeningHours(String restaurantId, List<TimeSlot> slots) {
        Restaurant restaurant = restaurantRepository.getById(restaurantId);

        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant does not exist");
        }

        RestaurantSchedule schedule = openingHoursRepository.getSchedule(restaurantId);
        
        // Retry loop for optimistic concurrency
        while (true) {
            ScheduleSnapshot current = schedule.getSnapshot();
            long newVersion = current.getVersion() + 1;
            
            // Update index first (read path uses index)
            OpeningSchedule openingSchedule = new OpeningSchedule(restaurantId, slots);
            availabilityIndex.update(restaurant, openingSchedule);
            
            // Try to update schedule atomically
            boolean success = schedule.update(newVersion, slots);
            
            if (success) {
                // Save to source of truth (for persistence)
                openingHoursRepository.saveSchedule(schedule);
                break;
            }
            
            // Version mismatch - rollback index and retry
            availabilityIndex.removeRestaurant(restaurant);
        }
    }
}
