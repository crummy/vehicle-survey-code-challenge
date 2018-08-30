package com.malcolmcrum.vehiclesurvey;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SensorPointFactory {

	private static final Pattern POINT_PATTERN = Pattern.compile("([AB])(\\d+)");
	private final List<SensorPoint> points;

	SensorPointFactory(List<String> data) throws ParsingException{
		this.points = data.stream()
				.map(this::parsePoint)
				.collect(Collectors.toList());
	}

	static SensorPointFactory parse(Path file) throws IOException, ParsingException {
		try(Stream<String> lines = Files.lines(file)) {
			return new SensorPointFactory(lines.collect(Collectors.toList()));
		}
	}

	private SensorPoint parsePoint(String line) throws ParsingException {
		Matcher matcher = POINT_PATTERN.matcher(line);
		if (!matcher.matches()) {
			throw new ParsingException(line);
		}
		char sensor = matcher.group(1).charAt(0);
		long millis = Long.parseLong(matcher.group(2));
		return new SensorPoint(sensor, millis);
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
