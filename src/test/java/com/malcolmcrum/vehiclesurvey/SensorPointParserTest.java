package com.malcolmcrum.vehiclesurvey;

import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

import static com.malcolmcrum.vehiclesurvey.ListHelper.listOf;
import static java.time.Instant.*;
import static java.time.ZoneOffset.UTC;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MILLIS;
import static org.assertj.core.api.Assertions.assertThat;

public class SensorPointParserTest {

	private static final Clock CLOCK = Clock.fixed(EPOCH, UTC);

	@Test
	public void multipleSensors() {
		List<String> sample = listOf("A0", "B1", "A9", "B20");

		List<SensorPoint> points = new SensorPointParser(CLOCK, sample).getPoints();

		assertThat(points).containsSequence(
				new SensorPoint('A', toInstant(0, 0)),
				new SensorPoint('B', toInstant(1L, 0)),
				new SensorPoint('A', toInstant(9L, 0)),
				new SensorPoint('B', toInstant(20L, 0))
		);
	}

	@Test
	public void singleDataPoint() {
		List<String> sample = listOf("A0");

		List<SensorPoint> points = new SensorPointParser(CLOCK, sample).getPoints();

		assertThat(points).containsOnly(new SensorPoint('A', toInstant(0L, 0)));
	}

	@Test
	public void sampleDataPoints() { // First six lines of sampledata.txt
		List<String> sample = listOf("A98186", "A98333", "A499718", "A499886", "A638379", "B638382", "A638520", "B638523");

		List<SensorPoint> points = new SensorPointParser(CLOCK, sample).getPoints();

		assertThat(points).containsSequence(
				new SensorPoint('A', toInstant(98186L, 0)),
				new SensorPoint('A', toInstant(98333L, 0)),
				new SensorPoint('A', toInstant(499718L, 0)),
				new SensorPoint('A', toInstant(499886L, 0)),
				new SensorPoint('A', toInstant(638379L, 0)),
				new SensorPoint('B', toInstant(638382L, 0)),
				new SensorPoint('A', toInstant(638520L, 0)),
				new SensorPoint('B', toInstant(638523L, 0))
		);
	}

	@Test
	public void nextDayParsing() {
		List<String> sample = listOf("A99", "A0");

		List<SensorPoint> points = new SensorPointParser(CLOCK, sample).getPoints();

		assertThat(points).containsSequence(
				new SensorPoint('A', toInstant(99L, 0)),
				new SensorPoint('A', toInstant(0L, 1))
		);
	}

	@Test
	public void emptyData() {
		List<String> noData = listOf();

		List<SensorPoint> points = new SensorPointParser(CLOCK, noData).getPoints();

		assertThat(points).isEmpty();
	}

	private Instant toInstant(long millis, int day) {
		return now(CLOCK).plus(millis, MILLIS).plus(day, DAYS);
	}
}