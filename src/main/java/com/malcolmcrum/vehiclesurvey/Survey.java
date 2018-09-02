package com.malcolmcrum.vehiclesurvey;

import com.malcolmcrum.vehiclesurvey.measures.Speed;

import java.util.List;

class Survey {

	private final List<Vehicle> vehicles;

	Survey(List<Vehicle> vehicles) {
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
