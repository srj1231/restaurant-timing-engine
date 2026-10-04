package com.saumya.lld.restaurant_timing_engine;

import com.saumya.lld.restaurant_timing_engine.entities.Day;
import com.saumya.lld.restaurant_timing_engine.entities.Restaurant;
import com.saumya.lld.restaurant_timing_engine.entities.TimeSlot;
import com.saumya.lld.restaurant_timing_engine.entities.availability.AvailabilityIndex;
import com.saumya.lld.restaurant_timing_engine.entities.availability.InMemoryAvailabilityIndex;
import com.saumya.lld.restaurant_timing_engine.repository.InMemoryOpeningHoursRepository;
import com.saumya.lld.restaurant_timing_engine.repository.InMemoryRestaurantRepository;
import com.saumya.lld.restaurant_timing_engine.repository.OpeningHoursRepository;
import com.saumya.lld.restaurant_timing_engine.repository.RestaurantRepository;
import com.saumya.lld.restaurant_timing_engine.service.RestaurantAvailabilityService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Time;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class RestaurantTimingEngineApplication {

	public static void main(String[] args) {
		RestaurantRepository restaurantRepository = new InMemoryRestaurantRepository();
		OpeningHoursRepository openingHoursRepository = new InMemoryOpeningHoursRepository();
		AvailabilityIndex availabilityIndex = new InMemoryAvailabilityIndex();

		RestaurantAvailabilityService restaurantAvailabilityService =
				new RestaurantAvailabilityService(restaurantRepository, openingHoursRepository, availabilityIndex);

		// create restaurants and save them
		Restaurant r1 = new Restaurant("r1", "Comorin", ZoneId.of("Asia/Kolkata"));
		Restaurant r2 = new Restaurant("r2", "Havemore", ZoneId.of("Asia/Kolkata"));
		Restaurant r3 = new Restaurant("r3", "Gulati's", ZoneId.of("Asia/Kolkata"));

		restaurantRepository.save(r1);
		restaurantRepository.save(r2);
		restaurantRepository.save(r3);

		// update opening hours
		restaurantAvailabilityService.updateOpeningHours(
				"r1",
				Arrays.asList(
						new TimeSlot(
								Day.MONDAY,
								LocalTime.of(9, 0),
								LocalTime.of(14, 0)
						),
						new TimeSlot(
								Day.MONDAY,
								LocalTime.of(18, 0),
								LocalTime.of(23, 0)
						)
				)
		);

		restaurantAvailabilityService.updateOpeningHours(
				"r2",
				List.of(
						new TimeSlot(
								Day.MONDAY,
								LocalTime.of(19, 0),
								LocalTime.of(23, 0)
						)
				)
		);

		restaurantAvailabilityService.updateOpeningHours(
				"r3",
                List.of(
                        new TimeSlot(
                                Day.MONDAY,
                                LocalTime.of(20, 0),
                                LocalTime.of(2, 0)
                        )
                )
		);

		// read 1: get r1's opening hours
		System.out.println("R1 opening hours: ");

		for(TimeSlot timeSlot : restaurantAvailabilityService.getOpeningHours("r1")) {
			System.out.println(timeSlot);
		}

		// read 2: get all restaurants open on Monday 11:00 IST
		ZonedDateTime monday11 =
				ZonedDateTime.of(2026, 10, 5, 11, 0, 0, 0, ZoneId.of("Asia/Kolkata")
		); // = Monday 11:00 IST

		System.out.println("\nAll restaurants open at Monday 11:00 IST: ");

		List<Restaurant> openRestaurants = restaurantAvailabilityService.getOpenRestaurants(monday11.toInstant());

		for (Restaurant restaurant : openRestaurants) {
			System.out.println(restaurant);
		}

		// read 2: get all restaurants open on Monday 19:00 IST
		ZonedDateTime monday19 =
				ZonedDateTime.of(2026, 10, 5, 19, 0, 0, 0, ZoneId.of("Asia/Kolkata")
				); // = Monday 19:00 IST

		System.out.println("\nAll restaurants open at Monday 19:00 IST: ");

		openRestaurants = restaurantAvailabilityService.getOpenRestaurants(monday19.toInstant());

		for (Restaurant restaurant : openRestaurants) {
			System.out.println(restaurant);
		}

		// read 2: get all restaurants open on Monday 23:00 IST
		ZonedDateTime monday23 =
				ZonedDateTime.of(2026, 10, 5, 23, 0, 0, 0, ZoneId.of("Asia/Kolkata")
				); // = Monday 23:00 IST

		System.out.println("\nAll restaurants open at Monday 23:00 IST: ");

		openRestaurants = restaurantAvailabilityService.getOpenRestaurants(monday23.toInstant());

		for (Restaurant restaurant : openRestaurants) {
			System.out.println(restaurant);
		}

		SpringApplication.run(RestaurantTimingEngineApplication.class, args);
	}

}
