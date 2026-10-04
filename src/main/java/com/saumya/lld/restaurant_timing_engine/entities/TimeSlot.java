package com.saumya.lld.restaurant_timing_engine.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TimeSlot {
    Day day;
    LocalTime openingTime;
    LocalTime closingTime;

    @Override
    public String toString() {
        return day + " " + openingTime + " - " + closingTime;
    }
}
