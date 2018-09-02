package com.malcolmcrum.vehiclesurvey;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

class SensorPoint {
	private final static long MILLIS_IN_DAY = Duration.of(1, ChronoUnit.DAYS).toMillis();

	private final char sensor;
	private final long millis;
	private final int day;

	SensorPoint(char sensor, long millis, int day) {
		this.sensor = sensor;
		this.millis = millis;
		this.day = day;
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
		SensorPoint point = (SensorPoint) o;
		return sensor == point.sensor &&
				millis == point.millis;
	}

	@Override
	public int hashCode() {
		return Objects.hash(sensor, millis);
	}

	int getDay() {
		return day;
	}

	long getMillis() {
		return millis;
	}

	long getTotalMillis() {
		return day * MILLIS_IN_DAY + millis;
	}

	public char getSensor() {
		return sensor;
	}
}
