package com.malcolmcrum.vehiclesurvey;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static com.malcolmcrum.vehiclesurvey.Vehicle.Direction.NORTHBOUND;
import static com.malcolmcrum.vehiclesurvey.Vehicle.Direction.SOUTHBOUND;
import static java.time.temporal.ChronoUnit.DAYS;

class App {
	private static final FileReader fileReader = new FileReader();
	private static final Clock DEFAULT_CLOCK = Clock.fixed(Instant.EPOCH, ZoneOffset.UTC);

	public static void main(String[] args) {
		try {
			validateArgs(args);

			Clock clock = parseClock(args).orElse(DEFAULT_CLOCK);
			List<String> data = fileReader.parse(args[0]);
			List<SensorPoint> sensorPoints = new SensorPointParser(clock, data).getPoints();
			List<Vehicle> vehicles = new VehicleFactory(sensorPoints).getVehicles();
			Survey survey = new Survey(vehicles, clock);

			print(survey, clock);
		} catch (Exception e) {
			e.printStackTrace();
			abort("An uncaught exception occurred: " + e);
		}
	}

	private static Optional<Clock> parseClock(String[] args) {
		if (args.length <= 2) {
			return Optional.empty();
		} else {
			return Optional.of(Clock.systemDefaultZone()); // TODO parse from args
		}
	}

	private static void validateArgs(String[] args) throws IOException {
		if (args.length == 0) {
			abort("An argument is missing. Usage:\n  <jar> <vehicledata.txt> [<startTime>]");
		}

		String file = args[0];
		Path path = Paths.get(file);
		if (Files.notExists(path)) {
			throw new IOException("The provided file does not exist: " + path);
		}

		if (args.length >= 2) {
			// TODO validate clock time
		}
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
		System.out.println("Third day, " + survey.getSummary(clock.instant().plus(2, DAYS), clock.instant().plus(2, DAYS)));
		System.out.println("Speed distribution: " + survey.getSpeedDistribution());
		System.out.println("Busiest hour: " + survey.getBusiestHour());
	}
}
