package com.malcolmcrum.vehiclesurvey;

import com.malcolmcrum.vehiclesurvey.measures.Length;
import com.malcolmcrum.vehiclesurvey.measures.Speed;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Vehicle {
	static final Length WHEELBASE = Length.fromMeters(2.5); // Distance between axles

	private final Instant firstSensor;
	private final List<Duration> sensorIntervals = new ArrayList<>();
	private final Direction direction;

	Vehicle(Instant axleStart, Instant secondAxleStart, Instant axleEnd, Instant secondAxleEnd, Direction direction) {
		this(axleStart, axleEnd, direction);
		this.sensorIntervals.add(Duration.between(secondAxleStart, secondAxleEnd));
	}

	Vehicle(Instant axleStart, Instant axleEnd, Direction direction) {
		this.firstSensor = axleStart;
		this.sensorIntervals.add(Duration.between(axleStart, axleEnd));
		this.direction = direction;
	}

	// At what instant does a car cross a physical point? For simplicity's sake I've decided it's the time the first sensor is triggered.
	Instant getFirstSensor() {
		return firstSensor;
	}

	Speed getAverageSpeed() {
		return new Speed(WHEELBASE, getAverageSensorInterval());
	}

	private Duration getAverageSensorInterval() {
		Duration total = sensorIntervals.stream().reduce(Duration.ZERO, Duration::plus);
		return total.dividedBy(sensorIntervals.size());
	}

	Speed getMaxSpeed() {
		return new Speed(WHEELBASE, getMaxSensorInterval());
	}

	private Duration getMaxSensorInterval() {
		return sensorIntervals.stream()
				.max(Duration::compareTo)
				.orElseThrow(() -> new RuntimeException("No sensor intervals found to find maximum for"));
	}

	public Direction getDirection() {
		return direction;
	}

	enum Direction {
		NORTHBOUND, SOUTHBOUND
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Vehicle vehicle = (Vehicle) o;
		return Objects.equals(sensorIntervals, vehicle.sensorIntervals) &&
				direction == vehicle.direction;
	}

	@Override
	public int hashCode() {
		return Objects.hash(sensorIntervals, direction);
	}

	@Override
	public String toString() {
		return direction.toString() + sensorIntervals;
	}
}