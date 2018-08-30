package com.malcolmcrum.vehiclesurvey;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.MILLIS;

class Vehicle {
	private final Instant time;
	private final List<Duration> sensorDurations = new ArrayList<>();

	Vehicle(Clock clock, long axleStart, long axleEnd, long secondAxleStart, long secondAxleEnd) {
		this(clock, axleStart, axleEnd);
		this.sensorDurations.add(durationBetween(secondAxleStart, secondAxleEnd));
	}

	Vehicle(Clock clock, long axleStart, long axleEnd) {
		this.time = Instant.now(clock).plus(axleStart, MILLIS);
		this.sensorDurations.add(durationBetween(axleStart, axleEnd));
	}

	private Duration durationBetween(long axleStart, long axleEnd) {
		Instant start = Instant.ofEpochMilli(axleStart);
		Instant end = Instant.ofEpochMilli(axleEnd);
		return Duration.between(start, end);
	}

	public Instant getTime() {
		return time;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Vehicle vehicle = (Vehicle) o;
		return Objects.equals(time, vehicle.time) &&
				Objects.equals(sensorDurations, vehicle.sensorDurations);
	}

	@Override
	public int hashCode() {
		return Objects.hash(time, sensorDurations);
	}
}