package com.malcolmcrum.vehiclesurvey;

import com.malcolmcrum.vehiclesurvey.measures.Length;
import com.malcolmcrum.vehiclesurvey.measures.Speed;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

/**
 * Given a list of Vehicles, returns statistical information about the fleet, e.g. average speed, cars per day.
 */
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

	Vehicle getFastestVehicle() {
		return vehicles.stream()
				.max(Comparator.comparing(Vehicle::getMaxSpeed))
				.orElseThrow(() -> new RuntimeException("No vehicles found to calculate max speed for"));
	}

	Map<Speed, Long> getSpeedDistribution() {
		return vehicles.stream()
				.map(Vehicle::getAverageSpeed)
				.collect(groupingBy(this::getSpeedIntervals, TreeMap::new, counting()));

	}

	private Speed getSpeedIntervals(Speed speed) {
		int roundedKph = (int)Math.round(speed.getKilometersPerHour() / 10) * 10;
		return Speed.fromKph(roundedKph);
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

	Length getAverageDistanceBetweenVehicles(Instant from, Instant until, Vehicle.Direction direction) {
		List<Vehicle> filtered = vehicles.stream()
				.filter(vehicle -> vehicle.getFirstSensor().isAfter(from))
				.filter(vehicle -> vehicle.getFirstSensor().isBefore(until))
				.filter(vehicle -> vehicle.getDirection().equals(direction))
				.collect(Collectors.toList());

		double totalMeters = 0;
		for (int i = 0; i < filtered.size() - 1; ++i) {
			Vehicle vehicle = filtered.get(i);
			Vehicle next = filtered.get(i+1);
			Duration betweenSensors = Duration.between(vehicle.getLastSensor(), next.getFirstSensor());
			Speed speed = vehicle.getAverageSpeed();
			//km/h = s
			// s * h = km
			double mmPerSecond = speed.getMetersPerSecond() * betweenSensors.toMillis();
			totalMeters += mmPerSecond / 1000;
		}
		double averageMeters = totalMeters / (filtered.size() - 1);
		return Length.fromMeters(averageMeters);
	}

	static class InvalidSurveyException extends RuntimeException {
		InvalidSurveyException(String message) {
			super(message);
		}
	}

	static class Summary {
		final int totalCars;
		final Speed averageKph;
		final Speed maxKph;

		Summary(List<Vehicle> vehicles) {
			this.totalCars = vehicles.size();
			double averageKph = vehicles.stream()
					.mapToDouble(vehicle -> vehicle.getAverageSpeed().getKilometersPerHour())
					.average()
					.orElse(0);
			this.averageKph = Speed.fromKph(averageKph);
			double maxKph = vehicles.stream()
					.mapToDouble(vehicle -> vehicle.getMaxSpeed().getKilometersPerHour())
					.max()
					.orElse(0);
			this.maxKph = Speed.fromKph(maxKph);
		}

		@Override
		public String toString() {
			return String.format("%d vehicles: %s average, %s max", totalCars, averageKph, maxKph);
		}
	}
}
