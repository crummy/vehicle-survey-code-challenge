package com.malcolmcrum.vehiclesurvey;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.malcolmcrum.vehiclesurvey.Vehicle.Direction.NORTHBOUND;
import static com.malcolmcrum.vehiclesurvey.Vehicle.Direction.SOUTHBOUND;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;

/**
 * Root class, designed to serve as a simple way to execute a survey against a test file.
 * Can also take a beginning date, otherwise it will use unix time 0.
 */
class App {
	public static void main(String[] args) {
		try {
			System.out.println("VEHICLE TRAFFIC ANALYSIS TOOL");
			System.out.println("-----------------------------");

			Configuration config = new Configuration(args);

			List<String> data = parse(config.getPath());
			List<SensorPoint> sensorPoints = new SensorPointParser(config.getClock(), data).getPoints();
			List<Vehicle> vehicles = new VehicleFactory(sensorPoints).getVehicles();
			Survey survey = new Survey(vehicles, config.getClock());

			print(survey, config.getClock());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("An uncaught exception occurred: " + e);
		}
	}

	private static List<String> parse(Path path) throws IOException {
		try (Stream<String> lines = Files.lines(path)) {
			return lines.collect(Collectors.toList());
		}
	}

	// The focus of this project has been data-processing work, and demonstrating the ability to return the results of particular queries,
	// rather than visualizing them in an interesting way. So I just print out some sample statistics I generated.
	private static void print(Survey survey, Clock clock) {
		System.out.println("Average speed of all vehicles: " + survey.getAverageSpeed());
		System.out.println("Total vehicles: " + survey.getTotalCars() + " (" + survey.getTotalCars(NORTHBOUND) + " northbound, " +
				survey.getTotalCars(SOUTHBOUND) + " southbound)");
		Vehicle fastestVehicle = survey.getFastestVehicle();
		System.out.println("Maximum speed: " + fastestVehicle.getMaxSpeed() + " at " + fastestVehicle.getFirstSensor());
		System.out.println("Cars per day: " + survey.getCarsPerDay());

		System.out.println("Third day: " + survey.getSummary(
				clock.instant().plus(2, DAYS),
				clock.instant().plus(3, DAYS)
		));
		System.out.println("Third day, morning rush hour: " + survey.getSummary(
				clock.instant().plus(2, DAYS).plus(8, HOURS),
				clock.instant().plus(2, DAYS).plus(10, HOURS)
		));


		System.out.println("Speed distribution: ");
		survey.getSpeedDistribution().forEach((speed, count) -> {
			System.out.println(String.format(" %.0fkph: %d cars", speed.getKilometersPerHour(), count));
		});
		System.out.println("Busiest hour: " + survey.getBusiestHour());

		System.out.println("Cars per hour:");
		survey.getSummaries().forEach((interval, summary) -> {
			StringBuilder builder = new StringBuilder(interval);
			builder.append(": ");
			// A bit of fudging to get the sample data set looking good for 80char terminals - would have to be smarter about printing the
			// right amount of lines to fit any data set
			float count = (float)summary.totalCars / survey.getTotalCars() * 20 * survey.getSummaries().size();
			for (int i = 0; i < count; ++i) {
				builder.append("|");
			}
			builder.append(" ");
			builder.append(summary.totalCars);
			builder.append(" (");
			builder.append(summary.averageKph);
			builder.append(" average)");
			System.out.println(builder);
		});
	}
}
