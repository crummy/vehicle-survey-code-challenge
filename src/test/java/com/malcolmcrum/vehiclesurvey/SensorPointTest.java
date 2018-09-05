package com.malcolmcrum.vehiclesurvey;

import org.junit.Test;

import java.time.Duration;
import java.time.Instant;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MILLIS;
import static org.assertj.core.api.Assertions.assertThat;

public class SensorPointTest {

	@Test
	public void testDayAddition() {
		SensorPoint dayZero = createSensorPoint('A', 0, 0);
		SensorPoint dayOne = createSensorPoint('A', 0, 1);

		Duration betweenSensors = Duration.between(dayZero.getInstant(), dayOne.getInstant());

		assertThat(betweenSensors).isEqualTo(Duration.of(1, DAYS));
	}

	@Test
	public void testDayAdditionOneWeekLater() {
		SensorPoint daySeven = createSensorPoint('A', 0, 7);
		SensorPoint dayEight = createSensorPoint('A', 0, 8);

		Duration betweenSensors = Duration.between(daySeven.getInstant(), dayEight.getInstant());

		assertThat(betweenSensors).isEqualTo(Duration.of(1, DAYS));
	}

	private SensorPoint createSensorPoint(char sensor, long millis, int day) {
		return new SensorPoint(sensor, Instant.now().plus(millis, MILLIS).plus(day, DAYS));
	}
}