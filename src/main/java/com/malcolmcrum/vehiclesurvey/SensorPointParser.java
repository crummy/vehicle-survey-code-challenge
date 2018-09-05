package com.malcolmcrum.vehiclesurvey;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MILLIS;

/**
 * Parses lines of data and generates SensorPoints from them.
 * The data is expected to be in a form like this:
 * A123
 * B124
 * The first character indicates the sensor index, and subsequent digits indicate milliseconds since midnight that the sensor was hit.
 * A day is calculated by detecting when consecutive milliseconds count down instead of up.
 * NOTE: This is not a perfect way to detect a day! For example, if at least one day goes past with no sensor readings, then the
 * day count will be inaccurate.
 */
class SensorPointParser {

	private static final Pattern POINT_PATTERN = Pattern.compile("([AB])(\\d+)");
	private final List<SensorPoint> points;
	private final Clock clock;
	private Long lastMillis = null; // Keep track of the previously recorded data so we can detect day changes
	private Integer lastDay = null;

	SensorPointParser(Clock clock, List<String> data) {
		this.clock = clock;
		this.points = data.stream()
				.map(this::parsePoint)
				.collect(Collectors.toList());
	}

	private SensorPoint parsePoint(String line) throws ParsingException {
		Matcher matcher = POINT_PATTERN.matcher(line);
		if (!matcher.matches()) {
			throw new ParsingException(line);
		}
		char sensor = matcher.group(1).charAt(0);
		long millis = Long.parseLong(matcher.group(2));
		int day = calculateDay(millis);
		Instant instant = toClockAdjustedInstant(millis, day);
		SensorPoint sensorPoint = new SensorPoint(sensor, instant);
		lastMillis = millis;
		lastDay = day;
		return sensorPoint;
	}

	private int calculateDay(long millis) {
		if (lastMillis == null && lastDay == null) {
			return 0;
		} else if (dayChanged(millis)) {
			return lastDay + 1;
		} else {
			return lastDay;
		}
	}

	private boolean dayChanged(long millis) {
		return lastMillis > millis;
	}

	private Instant toClockAdjustedInstant(long millis, int day) {
		return Instant.now(clock).plus(millis, MILLIS).plus(day, DAYS);
	}

	List<SensorPoint> getPoints() {
		return points;
	}

	private class ParsingException extends RuntimeException {
		ParsingException(String line) {
			super("Failed to parse data point: " + line);
		}
	}
}
