package com.malcolmcrum.vehiclesurvey;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VehicleFactory {
	private final Clock clock;
	private final List<SensorPoint> points;
	private List<Vehicle> vehicles = new ArrayList<>();

	public VehicleFactory(Clock clock, List<SensorPoint> sensorPoints) {
		this.clock = clock;
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
		return point.sensor == thirdPoint.sensor &&
				secondPoint.sensor == fourthPoint.sensor &&
				point.sensor != secondPoint.sensor;
	}

	private void addDoubleSensorVehicle(SensorPoint point, SensorPoint secondPoint, SensorPoint thirdPoint,
			SensorPoint fourthPoint) {
		Vehicle vehicle = new Vehicle(clock, point.millis, secondPoint.millis, thirdPoint.millis, fourthPoint.millis);
		vehicles.add(vehicle);
	}

	private boolean areSingleSensorPoints(SensorPoint point, SensorPoint nextPoint) {
		return point.sensor == nextPoint.sensor;
	}

	private void addSingleSensorVehicle(SensorPoint point, SensorPoint nextPoint) {
		Vehicle vehicle = new Vehicle(clock, point.millis, nextPoint.millis);
		vehicles.add(vehicle);
	}

	public List<Vehicle> getVehicles() {
		return vehicles;
	}

	static class VehicleParsingException extends RuntimeException {

		public VehicleParsingException(String reason, SensorPoint... points) {
			super(reason + " " + Arrays.toString(points));
		}
	}
}
