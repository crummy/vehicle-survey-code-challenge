package com.malcolmcrum.vehiclesurvey;

import java.time.Clock;
import java.util.List;

import static com.malcolmcrum.vehiclesurvey.Vehicle.Direction.NORTHBOUND;
import static com.malcolmcrum.vehiclesurvey.Vehicle.Direction.SOUTHBOUND;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;

class App {
	private static final FileReader fileReader = new FileReader();

	public static void main(String[] args) {
		try {
			System.out.println("VEHICLE TRAFFIC ANALYSIS TOOL");
			System.out.println("-----------------------------");

			Configuration config = new Configuration(args);

			List<String> data = fileReader.parse(config.getPath());
			List<SensorPoint> sensorPoints = new SensorPointParser(config.getClock(), data).getPoints();
			List<Vehicle> vehicles = new VehicleFactory(sensorPoints).getVehicles();
			Survey survey = new Survey(vehicles, config.getClock());

			print(survey, config.getClock());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("An uncaught exception occurred: " + e);
		}
	}

	private static void print(Survey survey, Clock clock) {
		System.out.println("Average speed of all vehicles: " + survey.getAverageSpeed());
		System.out.println("Total vehicles: " + survey.getTotalCars() + " (" + survey.getTotalCars(NORTHBOUND) + " northbound, " +
				survey.getTotalCars(SOUTHBOUND) + " southbound)");
		System.out.println("Maximum speed: " + survey.getMaxSpeed());
		System.out.println("Cars per day: " + survey.getCarsPerDay());

		System.out.println("Third day: " + survey.getSummary(
				clock.instant().plus(2, DAYS),
				clock.instant().plus(3, DAYS)
		));
		System.out.println("Third day, morning rush hour: " + survey.getSummary(
				clock.instant().plus(2, DAYS).plus(8, HOURS),
				clock.instant().plus(2, DAYS).plus(10, HOURS)
		));


		System.out.println("Speed distribution: " + survey.getSpeedDistribution());
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
			System.out.println(builder);
		});
	}
}
