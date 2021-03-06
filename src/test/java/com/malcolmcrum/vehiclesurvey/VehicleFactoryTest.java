package com.malcolmcrum.vehiclesurvey;

import org.junit.Ignore;
import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import static com.malcolmcrum.vehiclesurvey.ListHelper.listOf;
import static com.malcolmcrum.vehiclesurvey.Vehicle.Direction.NORTHBOUND;
import static com.malcolmcrum.vehiclesurvey.Vehicle.Direction.SOUTHBOUND;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MILLIS;
import static org.assertj.core.api.Assertions.assertThat;

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
	@Ignore
	public void simultaneousVehicles() {
		List<SensorPoint> mixedSensors = listOf(
				new SensorPoint('A', toInstant(0, 0)), // southbound
				new SensorPoint('A', toInstant(1, 0)), // northbound
				new SensorPoint('B', toInstant(2, 0)), // southbound
				new SensorPoint('A', toInstant(10, 0)), // southbound
				new SensorPoint('A', toInstant(11, 0)), // northbound
				new SensorPoint('B', toInstant(12, 0)) // southbound
		);

		List<Vehicle> vehicles = new VehicleFactory(mixedSensors).getVehicles();

		assertThat(vehicles).containsSequence(
				new Vehicle(Instant.ofEpochMilli(1), Instant.ofEpochMilli(11), NORTHBOUND),
				new Vehicle(Instant.ofEpochMilli(0), Instant.ofEpochMilli(2), Instant.ofEpochMilli(10), Instant.ofEpochMilli(12), SOUTHBOUND)
		);
	}

	private Instant toInstant(long millis, int day) {
		return Instant.now(CLOCK).plus(millis, MILLIS).plus(day, DAYS);
	}
}