package com.saumya.lld.restaurant_timing_engine.repository;

import com.saumya.lld.restaurant_timing_engine.entities.Restaurant;

import java.util.List;

public interface RestaurantRepository {

    Restaurant getById(String restaurantId);

    void save(Restaurant restaurant);

    List<Restaurant> findAll();
}
