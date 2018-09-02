package com.malcolmcrum.vehiclesurvey;

import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class VehicleFactoryTest {
	private static final Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

	@Test
	public void clockUsedForVehicle() {
		List<SensorPoint> points = listOf(new SensorPoint('A', 0, 0), new SensorPoint('A', 1, 0));
		List<Vehicle> vehicles = new VehicleFactory(clock, points).getVehicles();

		Vehicle vehicle = vehicles.get(0);

		assertThat(vehicle.getTime()).isEqualTo(clock.instant());
	}

	@Test
	public void singleSensorVehicle() {
		List<SensorPoint> points = listOf(new SensorPoint('A', 0, 0), new SensorPoint('A', 1, 0));
		List<Vehicle> vehicles = new VehicleFactory(clock, points).getVehicles();

		assertThat(vehicles).containsOnly(new Vehicle(clock, 0, 1));
	}

	private List<SensorPoint> listOf(SensorPoint... elements) {
		return Arrays.stream(elements).collect(Collectors.toList());
	}
}