package com.malcolmcrum.vehiclesurvey;

import org.junit.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class VehicleFactoryTest {
	@Test
	public void singleSensorVehicle() {
		List<SensorPoint> points = listOf(new SensorPoint('A', 0, 0), new SensorPoint('A', 1, 0));
		List<Vehicle> vehicles = new VehicleFactory(points).getVehicles();

		assertThat(vehicles).containsOnly(new Vehicle(Instant.ofEpochMilli(0), Instant.ofEpochMilli(1), Vehicle.Direction.NORTHBOUND));
	}

	@Test
	public void doubleSensorVehicle() {
		List<SensorPoint> points = listOf(
				new SensorPoint('A', 0, 0),
				new SensorPoint('B', 1, 0),
				new SensorPoint('A', 10, 0),
				new SensorPoint('B', 11, 0)
				);
		List<Vehicle> vehicles = new VehicleFactory(points).getVehicles();

		assertThat(vehicles).containsOnly(new Vehicle(Instant.ofEpochMilli(0), Instant.ofEpochMilli(1), Instant.ofEpochMilli(10),
				Instant.ofEpochMilli(11), Vehicle.Direction.NORTHBOUND));
	}

	private List<SensorPoint> listOf(SensorPoint... elements) {
		return Arrays.stream(elements).collect(Collectors.toList());
	}
}