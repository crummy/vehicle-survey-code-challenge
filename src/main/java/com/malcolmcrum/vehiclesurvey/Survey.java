package com.malcolmcrum.vehiclesurvey;

import com.malcolmcrum.vehiclesurvey.measures.Speed;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

	Summary getSummary(Instant from, Instant until) {
		List<Vehicle> filtered = vehicles.stream()
				.filter(vehicle -> vehicle.getFirstSensor().isAfter(from))
				.filter(vehicle -> vehicle.getFirstSensor().isBefore(until))
				.collect(Collectors.toList());

		return new Summary(filtered);
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

	Map<String, Long> getSpeedDistribution() {
		return vehicles.stream()
				.map(Vehicle::getAverageSpeed)
				.collect(groupingBy(this::getSpeedIntervals, TreeMap::new, counting()));

	}

	private String getSpeedIntervals(Speed speed) {
		int rounded = (int)(speed.getKilometersPerHour() / 10);
		return Integer.toString(rounded) + "0kph";
	}

	String getBusiestHour() {
		return vehicles.stream()
				.collect(groupingBy(this::getHourIntervals, TreeMap::new, counting()))
				.entrySet()
				.stream()
				.max(Comparator.comparingLong(Map.Entry::getValue))
				.map(entry -> entry.getKey() + " (" + entry.getValue() + " vehicles)")
				.orElseThrow(() -> new InvalidSurveyException("No vehicles found"));
	}

	private String getHourIntervals(Vehicle vehicle) {
		LocalDateTime dateTime = LocalDateTime.ofInstant(vehicle.getFirstSensor(), clock.getZone());
		return dateTime.toLocalTime().getHour() + ":00";
	}

	Map<String, Summary> getSummaries() {
		Map<String, List<Vehicle>> vehiclesPerHour = new TreeMap<>();
		for (Vehicle vehicle : vehicles) {
			String dayHourInterval = getDayHourIntervals(vehicle);
			List<Vehicle> vehicles = vehiclesPerHour.computeIfAbsent(dayHourInterval, interval -> new ArrayList<>());
			vehicles.add(vehicle);
		}
		return vehiclesPerHour.entrySet()
				.stream()
				.collect(Collectors.toMap(Map.Entry::getKey, entry -> new Summary(entry.getValue()), (entry, map) -> entry, TreeMap::new));

	}

	private String getDayHourIntervals(Vehicle vehicle) {
		LocalDateTime dateTime = LocalDateTime.ofInstant(vehicle.getFirstSensor(), clock.getZone());
		return String.format("%s, %2d:00", dateTime.toLocalDate(), dateTime.toLocalTime().getHour());
	}

	static class InvalidSurveyException extends RuntimeException {
		InvalidSurveyException(String message) {
			super(message);
		}
	}

	static class Summary {
		final int totalCars;
		final double averageKph;
		final double maxKph;

		Summary(List<Vehicle> vehicles) {
			this.totalCars = vehicles.size();
			this.averageKph = vehicles.stream()
					.mapToDouble(vehicle -> vehicle.getAverageSpeed().getKilometersPerHour())
					.average()
					.orElse(0);
			this.maxKph = vehicles.stream()
					.mapToDouble(vehicle -> vehicle.getMaxSpeed().getKilometersPerHour())
					.average()
					.orElse(0);
		}

		@Override
		public String toString() {
			return String.format("%d vehicles: %.1fkm/h average, %.1fkm/h max", totalCars, averageKph, maxKph);
		}
	}
}
