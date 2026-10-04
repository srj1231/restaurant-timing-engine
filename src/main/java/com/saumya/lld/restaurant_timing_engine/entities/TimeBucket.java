package com.saumya.lld.restaurant_timing_engine.entities;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@AllArgsConstructor
@FieldDefaults(makeFinal = true)
public class TimeBucket {
    Day day;
    int bucket;

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof TimeBucket timeBucket)) return false;

        return day == timeBucket.day &&
                bucket == timeBucket.bucket;
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, bucket);
    }
}
