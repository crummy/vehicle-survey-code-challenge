package com.malcolmcrum.vehiclesurvey;

import org.junit.Test;

import java.time.Clock;
import java.util.List;

import static com.malcolmcrum.vehiclesurvey.SensorPointParserTest.listOf;
import static org.assertj.core.api.Assertions.assertThat;

public class EndToEndTest {

	@Test
	public void testSingleSensorCar() {
		List<String> singleSensor = listOf("A98186", "A98333");

		List<SensorPoint> points = new SensorPointParser(Clock.systemUTC(), singleSensor).getPoints();
		List<Vehicle> vehicles = new VehicleFactory(points).getVehicles();
		Survey survey = new Survey(vehicles);

		assertThat(survey.getTotalCars()).isEqualTo(1);
		System.out.println(survey.getAverageSpeed().getKilometersPerHour());
		System.out.println(survey.getMaxSpeed().getKilometersPerHour());
	}
}
