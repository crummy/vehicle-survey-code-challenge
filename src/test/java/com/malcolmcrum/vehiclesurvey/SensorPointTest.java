package com.malcolmcrum.vehiclesurvey;

import org.junit.Test;

import java.time.Duration;
import java.time.Instant;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;

public class SensorPointTest {

	@Test
	public void testDayAddition() {
		SensorPoint dayZero = new SensorPoint('A', 0, 0);
		SensorPoint dayOne = new SensorPoint('A', 0, 1);

		Duration betweenSensors = Duration.between(dayZero.getInstant(), dayOne.getInstant());

		assertThat(betweenSensors).isEqualTo(Duration.of(1, DAYS));
	}

	@Test
	public void testDayAdditionOneWeekLater() {
		SensorPoint daySeven = new SensorPoint('A', 0, 7);
		SensorPoint dayEight = new SensorPoint('A', 0, 8);

		Duration betweenSensors = Duration.between(daySeven.getInstant(), dayEight.getInstant());

		assertThat(betweenSensors).isEqualTo(Duration.of(1, DAYS));
	}

	@Test
	public void testInstants() {
		SensorPoint zero = new SensorPoint('A', 0, 0);
		SensorPoint one = new SensorPoint('A', 1, 0);

		assertThat(zero.getInstant()).isEqualTo(Instant.ofEpochMilli(0));
		assertThat(one.getInstant()).isEqualTo(Instant.ofEpochMilli(1));
	}
}