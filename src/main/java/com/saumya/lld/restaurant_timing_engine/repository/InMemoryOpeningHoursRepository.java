package com.saumya.lld.restaurant_timing_engine.repository;

import com.saumya.lld.restaurant_timing_engine.entities.schedule.OpeningSchedule;
import com.saumya.lld.restaurant_timing_engine.entities.schedule.RestaurantSchedule;
import com.saumya.lld.restaurant_timing_engine.entities.schedule.ScheduleSnapshot;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryOpeningHoursRepository implements OpeningHoursRepository {

    private final Map<String, OpeningSchedule> openingHours = new ConcurrentHashMap<>();
    private final Map<String, RestaurantSchedule> schedules = new ConcurrentHashMap<>();

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
        schedules.remove(restaurantId);
    }

    @Override
    public RestaurantSchedule getSchedule(String restaurantId) {
        RestaurantSchedule schedule = schedules.get(restaurantId);

        if (schedule == null) {
            // Initialize with empty schedule if it does not exist
            ScheduleSnapshot initial = new ScheduleSnapshot(0, List.of());
            schedule = new RestaurantSchedule(initial);
            schedules.put(restaurantId, schedule);
        }

        return schedule;
    }

    @Override
    public void saveSchedule(RestaurantSchedule schedule) {
        // For in-memory, we don't need to do anything special
        // The schedule object is already updated in memory
        // In a real DB, we would persist the snapshot
    }
}
