package com.malcolmcrum.vehiclesurvey;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static com.malcolmcrum.vehiclesurvey.Vehicle.Direction.*;
import static java.time.temporal.ChronoUnit.DAYS;

class App {
	public static void main(String[] args) {
		try {
			validateArgs(args);
			Path file = validateFile(args[0]);

			List<SensorPoint> sensorPoints = SensorPointParser.parse(file).getPoints();
			List<Vehicle> vehicles = new VehicleFactory(sensorPoints).getVehicles();
			Clock clock = validateClock(args).orElse(Clock.systemUTC());
			Survey survey = new Survey(vehicles, clock);

			print(survey, clock);
		} catch (Exception e) {
			abort("An uncaught exception occurred: " + e.getMessage());
		}
	}

	private static Optional<Clock> validateClock(String[] args) {
		if (args.length <= 2) {
			return Optional.empty();
		} else {
			return Optional.of(Clock.fixed(Instant.EPOCH, ZoneId.systemDefault()));
		}
	}

	private static void validateArgs(String[] args) {
		if (args.length == 0) {
			abort("An argument is missing. Usage:\n  <jar> <vehicledata.txt> [<startTime>]");
		}
	}

	private static Path validateFile(String path) {
		Path file = Paths.get(path);
		if (Files.notExists(file)) {
			abort("The provided file does not exist: " + path);
		}
		return file;
	}

	private static void abort(String reason) {
		System.out.println(reason);
		System.exit(1);
	}

	private static void print(Survey survey, Clock clock) {
		System.out.println("Average speed of all vehicles: " + survey.getAverageSpeed().getKilometersPerHour() + "kph");
		System.out.println("Total vehicles: " + survey.getTotalCars() + " (" + survey.getTotalCars(NORTHBOUND) + " northbound, " +
				survey.getTotalCars(SOUTHBOUND) + " southbound)");
		System.out.println("Maximum speed: " + survey.getMaxSpeed().getKilometersPerHour() + "kph");
		System.out.println("Cars per day: " + survey.getCarsPerDay());
		System.out.println("Second day: " + survey.getSummary(clock.instant().plus(1, DAYS), clock.instant().plus(2, DAYS)));
	}
}
