package com.malcolmcrum.vehiclesurvey;

import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.malcolmcrum.vehiclesurvey.Vehicle.Direction.NORTHBOUND;
import static com.malcolmcrum.vehiclesurvey.Vehicle.Direction.SOUTHBOUND;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MILLIS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class VehicleFactoryTest {

	private static final Clock CLOCK = Clock.fixed(Instant.EPOCH, ZoneOffset.UTC);

	@Test
	public void singleSensorVehicle() {
		List<SensorPoint> points = listOf(
				new SensorPoint('A', toInstant(0, 0)),
				new SensorPoint('A', toInstant(1, 0))
		);
		List<Vehicle> vehicles = new VehicleFactory(points).getVehicles();

		assertThat(vehicles).containsOnly(new Vehicle(Instant.ofEpochMilli(0), Instant.ofEpochMilli(1), NORTHBOUND));
	}

	@Test
	public void doubleSensorVehicle() {
		List<SensorPoint> points = listOf(
				new SensorPoint('A', toInstant(0, 0)),
				new SensorPoint('B', toInstant(1, 0)),
				new SensorPoint('A', toInstant(10, 0)),
				new SensorPoint('B', toInstant(11, 0))
		);
		List<Vehicle> vehicles = new VehicleFactory(points).getVehicles();

		assertThat(vehicles).containsOnly(new Vehicle(Instant.ofEpochMilli(0), Instant.ofEpochMilli(1), Instant.ofEpochMilli(10),
				Instant.ofEpochMilli(11), SOUTHBOUND));
	}

	@Test
	public void twoVehiclesDifferentDays() {
		List<SensorPoint> points = listOf(
				new SensorPoint('A', toInstant(0, 0)),
				new SensorPoint('A', toInstant(1, 0)),
				new SensorPoint('A', toInstant(0, 1)),
				new SensorPoint('A', toInstant(1, 1)));
		List<Vehicle> vehicles = new VehicleFactory(points).getVehicles();

		assertThat(vehicles).containsSequence(
				new Vehicle(Instant.ofEpochMilli(0), Instant.ofEpochMilli(1), NORTHBOUND),
				new Vehicle(Instant.ofEpochMilli(0).plus(1, DAYS), Instant.ofEpochMilli(1).plus(1, DAYS), NORTHBOUND)
		);
	}

	@Test
	public void doubleSensorVehicleRealData() {
		List<SensorPoint> points = listOf(
				new SensorPoint('A', toInstant(638379, 0)),
				new SensorPoint('B', toInstant(638382, 0)),
				new SensorPoint('A', toInstant(638520, 0)),
				new SensorPoint('B', toInstant(638523, 0))
		);

		List<Vehicle> vehicles = new VehicleFactory(points).getVehicles();

		assertThat(vehicles).containsSequence(
				new Vehicle(Instant.ofEpochMilli(638379), Instant.ofEpochMilli(638382), Instant.ofEpochMilli(638520), Instant.ofEpochMilli(638523), SOUTHBOUND)
		);
	}

	// This test represents a bug in the parser - if two vehicles cross simultaneously, north and south, parsing will fail
	@Test
	public void simultaneousVehicles() {
		List<SensorPoint> mixedSensors = listOf(
				new SensorPoint('A', toInstant(0, 0)), // northbound
				new SensorPoint('A', toInstant(1, 0)), // southbound
				new SensorPoint('B', toInstant(2, 0)), // northbound
				new SensorPoint('A', toInstant(10, 0)), // northbound
				new SensorPoint('A', toInstant(11, 0)), // southbound
				new SensorPoint('B', toInstant(12, 0)) // northbound
		);

		assertThatThrownBy(() -> new VehicleFactory(mixedSensors).getVehicles()).isInstanceOf(VehicleFactory.VehicleParsingException.class);
	}

	private List<SensorPoint> listOf(SensorPoint... elements) {
		return Arrays.stream(elements).collect(Collectors.toList());
	}

	private Instant toInstant(long millis, int day) {
		return Instant.now(CLOCK).plus(millis, MILLIS).plus(day, DAYS);
	}
}