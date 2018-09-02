package com.malcolmcrum.vehiclesurvey;

import org.junit.Test;

import java.time.Duration;

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
}