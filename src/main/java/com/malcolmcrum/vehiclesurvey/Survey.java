package com.malcolmcrum.vehiclesurvey;

import com.malcolmcrum.vehiclesurvey.measures.Speed;

import java.util.List;

public class Survey {

	private final List<Vehicle> vehicles;

	public Survey(List<Vehicle> vehicles) {
		this.vehicles = vehicles;
		if (vehicles.isEmpty()) {
			throw new InvalidSurveyException("A survey cannot be performed on 0 vehicles");
		}
	}

	public Speed getAverageSpeed() {
		double totalMetersPerSecond = 0.0;
		for (Vehicle vehicle : vehicles) {
			double metersPerSecond = vehicle.getAverageSpeed().getMetersPerSecond();
			totalMetersPerSecond += metersPerSecond;
		}
		double averageMetersPerSecond = totalMetersPerSecond / vehicles.size();
		return new Speed(averageMetersPerSecond);
	}


	static class InvalidSurveyException extends RuntimeException {
		InvalidSurveyException(String message) {
			super(message);
		}
	}
}
