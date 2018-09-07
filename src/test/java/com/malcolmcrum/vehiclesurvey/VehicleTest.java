package com.malcolmcrum.vehiclesurvey;

import com.malcolmcrum.vehiclesurvey.measures.Speed;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;

import static com.malcolmcrum.vehiclesurvey.Vehicle.Direction.NORTHBOUND;
import static com.malcolmcrum.vehiclesurvey.Vehicle.Direction.SOUTHBOUND;
import static com.malcolmcrum.vehiclesurvey.Vehicle.WHEELBASE;
import static java.time.temporal.ChronoUnit.MILLIS;
import static org.assertj.core.api.Assertions.assertThat;

public class VehicleTest {

	@Test
	public void averageSpeedSingleSensor() {
		Vehicle vehicle = new Vehicle(millis(10), millis(20), NORTHBOUND);

		Speed averageSpeed = vehicle.getAverageSpeed();

		assertThat(averageSpeed).isEqualTo(new Speed(WHEELBASE, Duration.of(10, MILLIS)));
	}

	@Test
	public void averageIntervalDoubleSensor() {
		Vehicle vehicle = new Vehicle(millis(1), millis(2), millis(11), millis(32), NORTHBOUND);

		Speed averageSpeed = vehicle.getAverageSpeed();

		assertThat(averageSpeed).isEqualTo(new Speed(WHEELBASE, Duration.of(20, MILLIS)));
	}

	@Test
	public void maxSpeedSingleSensor() {
		Vehicle vehicle = new Vehicle(millis(10), millis(20), NORTHBOUND);

		Speed maxSpeed = vehicle.getMaxSpeed();

		assertThat(maxSpeed).isEqualTo(new Speed(WHEELBASE, Duration.of(10, MILLIS)));
	}

	@Test
	public void maxSpeedDoubleSensor() {
		Vehicle vehicle = new Vehicle(millis(1), millis(2), millis(11), millis(32), NORTHBOUND);

		Speed maxSpeed = vehicle.getMaxSpeed();

		assertThat(maxSpeed).isEqualTo(new Speed(WHEELBASE, Duration.of(30, MILLIS)));
	}

	@Test
	public void maxSpeedDoubleSensorRealData() {
		Vehicle vehicle = new Vehicle(millis(638379), millis(638382), millis(638520), millis(638523), SOUTHBOUND);

		assertThat(vehicle.getMaxSpeed()).isEqualTo(new Speed(WHEELBASE, Duration.of(141, MILLIS)));
	}

	private Instant millis(long ms) {
		return Instant.ofEpochMilli(ms);
	}
}