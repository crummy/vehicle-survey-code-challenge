package com.malcolmcrum.vehiclesurvey;

import com.malcolmcrum.vehiclesurvey.measures.Length;
import com.malcolmcrum.vehiclesurvey.measures.Speed;
import org.junit.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import static com.malcolmcrum.vehiclesurvey.ListHelper.listOf;
import static com.malcolmcrum.vehiclesurvey.Vehicle.Direction;
import static com.malcolmcrum.vehiclesurvey.Vehicle.Direction.NORTHBOUND;
import static com.malcolmcrum.vehiclesurvey.Vehicle.Direction.SOUTHBOUND;
import static com.malcolmcrum.vehiclesurvey.Vehicle.WHEELBASE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

public class SurveyTest {
	private static final Clock CLOCK = Clock.fixed(Instant.EPOCH, ZoneOffset.UTC);

	@Test
	public void averageSpeedCalculatedCorrectly() {
		List<Vehicle> vehicles = listOf(kph(50), kph(100));

		Speed average = new Survey(vehicles).getAverageSpeed();

		assertThat(average.getKilometersPerHour()).isEqualTo(75, offset(0.01));
	}

	@Test
	public void getTotalCars() {
		List<Vehicle> vehicles = listOf(vehicle(), vehicle(), vehicle());

		long total = new Survey(vehicles).getTotalCars();

		assertThat(total).isEqualTo(3);
	}

	@Test
	public void getTotalCarsPerDirection() {
		List<Vehicle> vehicles = listOf(vehicle(NORTHBOUND), vehicle(SOUTHBOUND), vehicle(SOUTHBOUND));

		Survey survey = new Survey(vehicles);
		long northbound = survey.getTotalCars(NORTHBOUND);
		long southbound = survey.getTotalCars(SOUTHBOUND);

		assertThat(northbound).isEqualTo(1);
		assertThat(southbound).isEqualTo(2);
	}

	@Test
	public void getFastestVehicle() {
		Vehicle fastest = kph(33);
		List<Vehicle> vehicles = listOf(kph(10), kph(20), fastest, kph(30));

		Vehicle fastestFromSurvey = new Survey(vehicles).getFastestVehicle();

		assertThat(fastestFromSurvey).isEqualTo(fastest);
	}

	@Test
	public void getAverageDistanceBetweenVehicles() {
		Direction direction = NORTHBOUND;
		Vehicle first = vehicle(100, direction);
		Vehicle oneSecondLater = vehicle(100, direction, Duration.ofSeconds(60));
		List<Vehicle> vehicles = listOf(first, oneSecondLater);

		Length distanceBetweenVehicles = new Survey(vehicles).getAverageDistanceBetweenVehicles(Instant.MIN, Instant.MAX, direction);

		Length expected = Speed.fromKph(100).toDistance(Duration.ofSeconds(60)).minus(WHEELBASE);
		assertThat(distanceBetweenVehicles.getMeters()).isEqualTo(expected.getMeters(), offset(0.01));
	}

	private Vehicle kph(int kph) {
		return vehicle(kph, SOUTHBOUND);
	}

	private Vehicle vehicle() {
		return kph(80);
	}

	private Vehicle vehicle(Direction direction) {
		return vehicle(80, direction);
	}

	private Vehicle vehicle(int kph, Direction direction) {
		return vehicle(kph, direction, Duration.ZERO);
	}

	private Vehicle vehicle(int kph, Direction direction, Duration offset) {
		Duration duration = Speed.fromKph(kph).toDuration(WHEELBASE);
		return new Vehicle(
				Instant.now(CLOCK).plus(offset),
				Instant.now(CLOCK).plus(offset).plus(duration),
				direction
		);
	}
}