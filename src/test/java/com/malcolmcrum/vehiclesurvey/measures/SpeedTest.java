package com.malcolmcrum.vehiclesurvey.measures;

import org.junit.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

public class SpeedTest {
	@Test
	public void testKphConversion() {
		Speed oneMeterPerSecond = new Speed(Length.METER, Duration.ofSeconds(1));

		double kph = oneMeterPerSecond.getKilometersPerHour();

		assertThat(kph).isEqualTo(3.6);
	}

	@Test
	public void testMeterPerSecond() {
		Speed oneMeterPerSecond = new Speed(Length.METER, Duration.ofSeconds(1));

		double mps = oneMeterPerSecond.getMetersPerSecond();

		assertThat(mps).isEqualTo(1.0);
	}

	@Test
	public void fiftyKphConversion() {
		Speed fiftyKph = new Speed(Length.fromMeters(50_000), Duration.ofHours(1));

		double kph = fiftyKph.getKilometersPerHour();

		assertThat(kph).isEqualTo(50, offset(0.01));
	}
}