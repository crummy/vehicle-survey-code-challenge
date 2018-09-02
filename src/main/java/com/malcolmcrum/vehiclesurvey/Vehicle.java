package com.malcolmcrum.vehiclesurvey;

import com.malcolmcrum.vehiclesurvey.measures.Length;
import com.malcolmcrum.vehiclesurvey.measures.Speed;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.MILLIS;

class Vehicle {
	private static final Length WHEELBASE = Length.fromMeters(2.5); // Distance between axles

	private final Instant time;
	private final List<Duration> sensorIntervals = new ArrayList<>();
	private final Direction direction;

	Vehicle(Clock clock, long axleStart, long axleEnd, long secondAxleStart, long secondAxleEnd, Direction direction) {
		this(clock, axleStart, axleEnd, direction);
		this.sensorIntervals.add(durationBetween(secondAxleStart, secondAxleEnd));
	}

	Vehicle(Clock clock, long axleStart, long axleEnd, Direction direction) {
		this.time = Instant.now(clock).plus(axleStart, MILLIS);
		this.sensorIntervals.add(durationBetween(axleStart, axleEnd));
		this.direction = direction;
	}

	private Duration durationBetween(long axleStart, long axleEnd) {
		Instant start = Instant.ofEpochMilli(axleStart);
		Instant end = Instant.ofEpochMilli(axleEnd);
		return Duration.between(start, end);
	}

	Instant getTime() {
		return time;
	}

	Speed getAverageSpeed() {
		return new Speed(WHEELBASE, getAverageSensorInterval());
	}

	Duration getAverageSensorInterval() {
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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Vehicle vehicle = (Vehicle) o;
		return Objects.equals(time, vehicle.time) &&
				Objects.equals(sensorIntervals, vehicle.sensorIntervals);
	}

	@Override
	public int hashCode() {
		return Objects.hash(time, sensorIntervals);
	}

	public Direction getDirection() {
		return direction;
	}

	enum Direction {
		NORTHBOUND, SOUTHBOUND
	}
}