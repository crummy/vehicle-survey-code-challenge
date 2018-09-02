package com.malcolmcrum.vehiclesurvey;

import org.junit.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class VehicleTest {

	@Test
	public void averageDurationSingleSensor() {
		Vehicle vehicle = new Vehicle(Clock.systemUTC(), 10, 20);

		Duration averageDuration = vehicle.getAverageSensorInterval();

		assertThat(averageDuration).isEqualTo(Duration.of(10, ChronoUnit.MILLIS));
	}

	@Test
	public void averageDurationDoubleSensor() {
		Vehicle vehicle = new Vehicle(Clock.systemUTC(), 10, 20, 40, 60);

		Duration averageDuration = vehicle.getAverageSensorInterval();

		assertThat(averageDuration).isEqualTo(Duration.of(15, ChronoUnit.MILLIS));
	}
}