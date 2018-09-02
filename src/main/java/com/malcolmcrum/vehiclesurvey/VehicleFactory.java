package com.malcolmcrum.vehiclesurvey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class VehicleFactory {
	private final List<SensorPoint> points;
	private final List<Vehicle> vehicles = new ArrayList<>();

	public VehicleFactory(List<SensorPoint> sensorPoints) {
		this.points = sensorPoints;
		calculateVehicles();
	}

	private void calculateVehicles() {
		for (int i = 0; i < points.size(); ++i) {
			SensorPoint point = points.get(i);
			SensorPoint secondPoint = points.get(i + 1);
			if (areSingleSensorPoints(point, secondPoint)) {
				i++;
				addSingleSensorVehicle(point, secondPoint);
				continue;
			}
			SensorPoint thirdPoint = points.get(i + 2);
			SensorPoint fourthPoint = points.get(i + 3);
			if (areDoubleSensorPoints(point, secondPoint, thirdPoint, fourthPoint)) {
				i += 3;
				addDoubleSensorVehicle(point, secondPoint, thirdPoint, fourthPoint);
				continue;
			}
			throw new VehicleParsingException("Couldn't distinguish between single and double sensor", point, secondPoint, thirdPoint, fourthPoint);
		}
	}

	private boolean areDoubleSensorPoints(SensorPoint point, SensorPoint secondPoint, SensorPoint thirdPoint,
			SensorPoint fourthPoint) {
		return point.getSensor() == thirdPoint.getSensor() &&
				secondPoint.getSensor() == fourthPoint.getSensor() &&
				point.getSensor() != secondPoint.getSensor();
	}

	private void addDoubleSensorVehicle(SensorPoint point, SensorPoint secondPoint, SensorPoint thirdPoint,
			SensorPoint fourthPoint) {
		Vehicle vehicle = new Vehicle(point.getInstant(), secondPoint.getInstant(), thirdPoint.getInstant(),
				fourthPoint.getInstant(), Vehicle.Direction.SOUTHBOUND);
		vehicles.add(vehicle);
	}

	private boolean areSingleSensorPoints(SensorPoint point, SensorPoint nextPoint) {
		return point.getSensor() == nextPoint.getSensor();
	}

	private void addSingleSensorVehicle(SensorPoint point, SensorPoint nextPoint) {
		Vehicle vehicle = new Vehicle(point.getInstant(), nextPoint.getInstant(), Vehicle.Direction.NORTHBOUND);
		vehicles.add(vehicle);
	}

	List<Vehicle> getVehicles() {
		return vehicles;
	}

	static class VehicleParsingException extends RuntimeException {
		VehicleParsingException(String reason, SensorPoint... points) {
			super(reason + " " + Arrays.toString(points));
		}
	}
}
