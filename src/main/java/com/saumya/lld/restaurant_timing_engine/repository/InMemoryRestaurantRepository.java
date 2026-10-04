package com.saumya.lld.restaurant_timing_engine.repository;

import com.saumya.lld.restaurant_timing_engine.entities.Restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRestaurantRepository implements RestaurantRepository {

    private final Map<String, Restaurant> restaurants = new ConcurrentHashMap<>();

    @Override
    public Restaurant get(String restaurantId) {
        return restaurants.get(restaurantId);
    }

    @Override
    public void save(Restaurant restaurant) {
        restaurants.put(restaurant.getId(), restaurant);
    }

    @Override
    public List<Restaurant> findAll() {
        return new ArrayList<>(restaurants.values());
    }
}
