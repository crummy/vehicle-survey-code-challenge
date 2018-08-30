package com.malcolmcrum.vehiclesurvey;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SensorData {

	private static final Pattern POINT_PATTERN = Pattern.compile("[AB]\\d+");
	private final List<Point> points;

	SensorData(List<String> data) throws ParsingException{
		this.points = data.stream()
				.map(this::parsePoint)
				.collect(Collectors.toList());
	}

	public static SensorData parse(Path file) throws IOException, ParsingException {
		try(Stream<String> lines = Files.lines(file)) {
			return new SensorData(lines.collect(Collectors.toList()));
		}
	}

	private Point parsePoint(String line) throws ParsingException {
		Matcher matcher = POINT_PATTERN.matcher(line);
		if (!matcher.matches()) {
			throw new ParsingException(line);
		}
		char sensor = matcher.group(0).charAt(0);
		long millis = Long.parseLong(matcher.group(1));
		return new Point(sensor, millis);
	}

	public List<Point> getPoints() {
		return points;
	}

	static class Point {
		final char sensor;
		final Long millis;

		Point(char sensor, Long millis) {
			this.sensor = sensor;
			this.millis = millis;
		}
	}

	private class ParsingException extends RuntimeException {
		ParsingException(String line) {
			super("Failed to parse data point: " + line);
		}
	}
}
