package com.malcolmcrum.vehiclesurvey;

import java.time.Instant;
import java.util.Objects;

class SensorPoint {
	private final char sensor;
	private final Instant instant;

	SensorPoint(char sensor, Instant instant) {
		this.sensor = sensor;
		this.instant = instant;
	}

	Instant getInstant() {
		return instant;
	}

	char getSensor() {
		return sensor;
	}

	@Override
	public String toString() {
		return sensor + " " + instant;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		SensorPoint that = (SensorPoint) o;
		return sensor == that.sensor &&
				Objects.equals(instant, that.instant);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sensor, instant);
	}
}
