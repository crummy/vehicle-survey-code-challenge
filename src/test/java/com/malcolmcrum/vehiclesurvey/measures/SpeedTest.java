package com.malcolmcrum.vehiclesurvey.measures;

import org.junit.Test;

import java.time.Duration;

import static com.malcolmcrum.vehiclesurvey.measures.Length.*;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

public class SpeedTest {
	@Test
	public void testKphConversion() {
		Speed oneMeterPerSecond = new Speed(METER, Duration.ofSeconds(1));

		double kph = oneMeterPerSecond.getKilometersPerHour();

		assertThat(kph).isEqualTo(3.6);
	}

	@Test
	public void testMeterPerSecond() {
		Speed oneMeterPerSecond = new Speed(METER, Duration.ofSeconds(1));

		double mps = oneMeterPerSecond.getMetersPerSecond();

		assertThat(mps).isEqualTo(1.0);
	}

	@Test
	public void fiftyKphConversion() {
		Speed fiftyKph = new Speed(fromMeters(50_000), Duration.ofHours(1));

		double kph = fiftyKph.getKilometersPerHour();

		assertThat(kph).isEqualTo(50, offset(0.01));
	}

	@Test
	public void oneMeterPerSecondInOneSecond() {
		Speed oneMeterPerSecond = new Speed(METER, Duration.ofSeconds(1));
		Length oneMeter = Length.fromMeters(1);

		Duration time = oneMeterPerSecond.toDuration(oneMeter);

		assertThat(time).isEqualTo(Duration.of(1, SECONDS));
	}

	@Test
	public void fiftyKphInOneHour() {
		Speed fiftyKph = Speed.fromKph(50);
		Length oneKilometer = Length.fromKilometers(50);

		Duration time = fiftyKph.toDuration(oneKilometer);

		assertThat(time).isEqualTo(Duration.of(1, HOURS));
	}
}