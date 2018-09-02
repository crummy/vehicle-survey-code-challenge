package com.malcolmcrum.vehiclesurvey.measures;

import org.junit.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class SpeedTest {
	@Test
	public void testKphConversion() {
		Speed oneMeterPerSecond = new Speed(Length.METER, Duration.ofSeconds(1));

		double kph = oneMeterPerSecond.getKilometersPerHour();

		assertThat(kph).isEqualTo(3.6);
	}
}