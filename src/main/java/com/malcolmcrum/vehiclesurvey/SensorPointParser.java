package com.malcolmcrum.vehiclesurvey;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
	private SensorPoint lastPoint = null; // Keep track of the previously recorded point so we can detect day changes

	SensorPointParser(List<String> data) throws ParsingException{
		this.points = data.stream()
				.map(this::parsePoint)
				.collect(Collectors.toList());
	}

	static SensorPointParser parse(Path file) throws IOException, ParsingException {
		try (Stream<String> lines = Files.lines(file)) {
			return new SensorPointParser(lines.collect(Collectors.toList()));
		}
	}

	private SensorPoint parsePoint(String line) throws ParsingException {
		Matcher matcher = POINT_PATTERN.matcher(line);
		if (!matcher.matches()) {
			throw new ParsingException(line);
		}
		char sensor = matcher.group(1).charAt(0);
		long millis = Long.parseLong(matcher.group(2));
		int day;
		if (lastPoint == null) {
			day = 0;
		} else if (dayChanged(millis)) {
			day = lastPoint.getDay() + 1;
		} else {
			day = lastPoint.getDay();
		}
		SensorPoint sensorPoint = new SensorPoint(sensor, millis, day);
		lastPoint = sensorPoint;
		return sensorPoint;
	}

	private boolean dayChanged(long millis) {
		return lastPoint.getMillis() > millis;
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
