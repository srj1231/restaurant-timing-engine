package com.saumya.lld.restaurant_timing_engine.entities.schedule;

import com.saumya.lld.restaurant_timing_engine.entities.TimeSlot;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OpeningSchedule {

    @Getter
    String restaurantId;
    List<TimeSlot> slots;

    public OpeningSchedule(String restaurantId, List<TimeSlot> slots) {
        this.restaurantId = restaurantId;
        this.slots = new ArrayList<>(slots);
    }

    public List<TimeSlot> getSlots() {
        return Collections.unmodifiableList(slots);
    }
}
