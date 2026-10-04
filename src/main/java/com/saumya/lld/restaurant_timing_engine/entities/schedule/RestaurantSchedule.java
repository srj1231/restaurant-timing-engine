package com.saumya.lld.restaurant_timing_engine.entities.schedule;

import com.saumya.lld.restaurant_timing_engine.entities.TimeSlot;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Getter
public class RestaurantSchedule {

    private final AtomicReference<ScheduleSnapshot> current;

    public RestaurantSchedule(ScheduleSnapshot initial) {
        this.current = new AtomicReference<>(initial);
    }

    public ScheduleSnapshot getSnapshot() {
        return current.get();
    }

    public boolean update(long newVersion, List<TimeSlot> newSlots) {
        // optimistic concurrency control
        ScheduleSnapshot old = current.get();

        if(old.getVersion() != newVersion) {
            System.out.println("Version mismatch");
            return false;
        }

        ScheduleSnapshot newSnapshot = new ScheduleSnapshot(newVersion, newSlots);
        current.set(newSnapshot);   // atomic
        return true;
    }
}
