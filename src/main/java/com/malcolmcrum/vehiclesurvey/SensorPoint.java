package com.malcolmcrum.vehiclesurvey;

import java.time.Instant;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.DAYS;

class SensorPoint {
	private final char sensor;
	private final long millis;
	private final int day;

	SensorPoint(char sensor, long millis, int day) {
		this.sensor = sensor;
		this.millis = millis;
		this.day = day;
	}

	int getDay() {
		return day;
	}

	long getMillis() {
		return millis;
	}

	Instant getInstant() {
		return Instant.ofEpochMilli(millis).plus(day, DAYS);
	}

	char getSensor() {
		return sensor;
	}

	@Override
	public String toString() {
		return sensor + Long.toString(millis);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		SensorPoint that = (SensorPoint) o;
		return sensor == that.sensor &&
				millis == that.millis &&
				day == that.day;
	}

	@Override
	public int hashCode() {
		return Objects.hash(sensor, millis, day);
	}
}
