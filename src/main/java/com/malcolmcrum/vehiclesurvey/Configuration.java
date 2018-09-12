package com.malcolmcrum.vehiclesurvey;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static java.time.Instant.EPOCH;
import static java.time.ZoneOffset.UTC;

/**
 * Validates input, and stores configuration information for the app for later retrieval
 */
class Configuration {
	private static final Clock DEFAULT_CLOCK = Clock.fixed(EPOCH, UTC);

	private final Path path;
	private final Clock clock;

	Configuration(String[] args) {
		path = validateDataFile(args);
		clock = validateClock(args);
	}

	private Clock validateClock(String[] args) {
		if (args.length >= 2) {
			LocalDate date;
			try {
				date = LocalDate.parse(args[1]);
			} catch (DateTimeParseException e) {
				throw new ConfigurationException("Failed to validate clock parameter '" + args[1] + "' - please format like 2001-12-31", e);
			}
			Instant instant = date.atStartOfDay().toInstant(UTC);
			return Clock.fixed(instant, UTC);
		} else {
			return null;
		}
	}

	private Path validateDataFile(String[] args) {
		if (args.length == 0) {
			throw new ConfigurationException("An argument is missing. Usage:\n  <jar> <vehicledata.txt> [<startTime>]");
		}

		String file = args[0];
		Path path = Paths.get(file);
		if (Files.notExists(path)) {
			throw new ConfigurationException("The provided file does not exist: " + path);
		}
		return path;
	}

	public Path getPath() {
		return path;
	}

	public Clock getClock() {
		return clock != null ? clock : DEFAULT_CLOCK;
	}

	static class ConfigurationException extends RuntimeException {
		ConfigurationException(String message) {
			super(message);
		}

		ConfigurationException(String message, Exception cause) {
			super(message, cause);
		}
	}
}
