package com.saumya.lld.restaurant_timing_engine.entities.schedule;

import com.saumya.lld.restaurant_timing_engine.entities.TimeSlot;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ScheduleSnapshot {
    long version;   // for optimistic concurrency control
    List<TimeSlot> slots;

    public ScheduleSnapshot(long version, List<TimeSlot> slots) {
        this.version = version;
        this.slots = List.copyOf(slots); // immutable
    }
}
