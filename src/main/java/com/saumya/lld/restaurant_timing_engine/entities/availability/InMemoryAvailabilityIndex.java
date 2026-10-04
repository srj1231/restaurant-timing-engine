package com.saumya.lld.restaurant_timing_engine.entities.availability;

import com.saumya.lld.restaurant_timing_engine.entities.*;
import com.saumya.lld.restaurant_timing_engine.entities.schedule.OpeningSchedule;
import com.saumya.lld.restaurant_timing_engine.repository.RestaurantRepository;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAvailabilityIndex implements AvailabilityIndex {

    private static final int BUCKET_SIZE = 15;

    // MONDAY 36 = 36 * 15 minutes = 9:00
    // MONDAY 36 -> [R1, R3, R4]
    private final Map<TimeBucket, Set<String>> availabilityIndex = new ConcurrentHashMap<>();

    // so updating slots is easier
    private final Map<String, Set<TimeBucket>> restaurantBuckets = new ConcurrentHashMap<>();

    public Set<String> getOpenRestaurants(
            Instant instant,
            RestaurantRepository restaurantRepository
    ) {
        Set<String> openRestaurants = new HashSet<>();

        for(Restaurant restaurant : restaurantRepository.findAll()) {
            ZonedDateTime local = instant.atZone(restaurant.getTimeZone());
            TimeBucket bucket = createBucket(local);
            Set<String> restaurants = availabilityIndex.get(bucket);
            if(restaurants != null && restaurants.contains(restaurant.getId())) {
                openRestaurants.addAll(restaurants);
            }
        }
        return openRestaurants;
    }

    private TimeBucket createBucket(ZonedDateTime dateTime) {
        int bucket = getBucket(dateTime.toLocalTime());
        Day day = Day.valueOf(dateTime.getDayOfWeek().name());
        
        return new TimeBucket(day, bucket); 
    }

    private int getBucket(LocalTime time) {
        int minutes = time.getHour() * 60 + time.getMinute();
        return minutes / BUCKET_SIZE;
    }

    @Override
    public void update(Restaurant restaurant, OpeningSchedule openingSchedule) {
        removeRestaurant(restaurant);

        Set<TimeBucket> buckets = new HashSet<>();
        for(TimeSlot slot : openingSchedule.getSlots()) {
            addSlot(restaurant, slot, buckets);
        }

        restaurantBuckets.put(restaurant.getId(), buckets);
    }

    private void addSlot(Restaurant restaurant, TimeSlot slot, Set<TimeBucket> buckets) {
        LocalTime open = slot.getOpeningTime();
        LocalTime close = slot.getClosingTime();

        if(!close.isBefore(open)) {
            addInterval(slot.getDay(), open, close, restaurant.getId(), buckets);
        }
        else {
            addInterval(slot.getDay(), open, LocalTime.MAX, restaurant.getId(), buckets);

            Day nextDay = nextDay(slot.getDay());
            addInterval(nextDay, LocalTime.MIDNIGHT, close, restaurant.getId(), buckets);
        }
    }

    private Day nextDay(Day day) {
        Day[] days = Day.values();
        return days[(day.ordinal() + 1) % days.length];
    }

    private void addInterval(Day day, LocalTime open, LocalTime close, String id, Set<TimeBucket> buckets) {
        int startBucket = getBucket(open);
        int endBucket = getBucket(close);

        for(int bucket = startBucket; bucket < endBucket; bucket++) {
            TimeBucket timeBucket = new TimeBucket(day, bucket);

            availabilityIndex.computeIfAbsent(
                    timeBucket,
                    k -> ConcurrentHashMap.newKeySet()
            ).add(id);

            buckets.add(timeBucket);
        }
    }

    @Override
    public void removeRestaurant(Restaurant restaurant) {
        Set<TimeBucket> buckets = restaurantBuckets.remove(restaurant.getId());
        if(buckets == null) return;

        for(TimeBucket bucket : buckets) {
            Set<String> restaurants = availabilityIndex.get(bucket);
            if(restaurants == null) continue;

            restaurants.remove(restaurant.getId());

            if(restaurants.isEmpty()) {
                availabilityIndex.remove(bucket);
            }
        }
    }
}
