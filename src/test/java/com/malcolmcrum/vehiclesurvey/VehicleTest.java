package com.malcolmcrum.vehiclesurvey;

import com.malcolmcrum.vehiclesurvey.measures.Speed;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.malcolmcrum.vehiclesurvey.Vehicle.Direction.NORTHBOUND;
import static com.malcolmcrum.vehiclesurvey.Vehicle.WHEELBASE;
import static org.assertj.core.api.Assertions.assertThat;

public class VehicleTest {

	private static final Instant _10_MS = Instant.ofEpochMilli(10);
	private static final Instant _20_MS = Instant.ofEpochMilli(20);
	private static final Instant _40_MS = Instant.ofEpochMilli(40);
	private static final Instant _60_MS = Instant.ofEpochMilli(60);

	@Test
	public void averageSpeedSingleSensor() {
		Vehicle vehicle = new Vehicle(_10_MS, _20_MS, NORTHBOUND);

		Speed averageSpeed = vehicle.getAverageSpeed();

		assertThat(averageSpeed).isEqualTo(new Speed(WHEELBASE, Duration.of(10, ChronoUnit.MILLIS)));
	}

	@Test
	public void averageIntervalDoubleSensor() {
		Vehicle vehicle = new Vehicle(_10_MS, _20_MS, _40_MS, _60_MS, NORTHBOUND);

		Speed averageSpeed = vehicle.getAverageSpeed();

		assertThat(averageSpeed).isEqualTo(new Speed(WHEELBASE, Duration.of(15, ChronoUnit.MILLIS)));
	}

	@Test
	public void maxSpeedSingleSensor() {
		Vehicle vehicle = new Vehicle(_10_MS, _20_MS, NORTHBOUND);

		Speed maxSpeed = vehicle.getMaxSpeed();

		assertThat(maxSpeed).isEqualTo(new Speed(WHEELBASE, Duration.of(10, ChronoUnit.MILLIS)));
	}

	@Test
	public void maxSpeedDoubleSensor() {
		Vehicle vehicle = new Vehicle(_10_MS, _20_MS, _40_MS, _60_MS, NORTHBOUND);

		Speed maxSpeed = vehicle.getMaxSpeed();

		assertThat(maxSpeed).isEqualTo(new Speed(WHEELBASE, Duration.of(20, ChronoUnit.MILLIS)));
	}
}