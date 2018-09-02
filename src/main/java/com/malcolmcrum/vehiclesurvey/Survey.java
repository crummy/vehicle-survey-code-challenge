package com.malcolmcrum.vehiclesurvey;

import com.malcolmcrum.vehiclesurvey.measures.Speed;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

class Survey {

	private final List<Vehicle> vehicles;
	private final Clock clock;

	Survey(List<Vehicle> vehicles) {
		this(vehicles, Clock.systemUTC());
	}

	Survey(List<Vehicle> vehicles, Clock clock) {
		this.clock = clock;
		this.vehicles = vehicles;
		if (vehicles.isEmpty()) {
			throw new InvalidSurveyException("A survey cannot be performed on 0 vehicles");
		}
	}

	Speed getAverageSpeed() {
		double totalMetersPerSecond = 0.0;
		for (Vehicle vehicle : vehicles) {
			double metersPerSecond = vehicle.getAverageSpeed().getMetersPerSecond();
			totalMetersPerSecond += metersPerSecond;
		}
		double averageMetersPerSecond = totalMetersPerSecond / vehicles.size();
		return new Speed(averageMetersPerSecond);
	}

	long getTotalCars() {
		return vehicles.size();
	}

	long getTotalCars(Vehicle.Direction direction) {
		return vehicles.stream()
				.filter(vehicle -> vehicle.getDirection().equals(direction))
				.count();
	}

	Map<String, Long> getCarsPerDay() {
		 return vehicles.stream()
				 .collect(groupingBy(this::getDayName, TreeMap::new, counting()));
	}

	private String getDayName(Vehicle vehicle) {
		LocalDateTime dateTime = LocalDateTime.ofInstant(vehicle.getFirstSensor(), clock.getZone());
		return dateTime.toLocalDate().toString();
	}

	Speed getMaxSpeed() {
		return vehicles.stream()
				.map(Vehicle::getMaxSpeed)
				.max(Speed::compareTo)
				.orElseThrow(() -> new RuntimeException("No vehicles found to calculate max speed for"));
	}

	static class InvalidSurveyException extends RuntimeException {
		InvalidSurveyException(String message) {
			super(message);
		}
	}
}
