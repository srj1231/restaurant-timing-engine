package com.saumya.lld.restaurant_timing_engine.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.ZoneId;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Restaurant {
    String id;
    String name;
    ZoneId timeZone;

    @Override
    public String toString() {
        return id + " - " + name;
    }
}
