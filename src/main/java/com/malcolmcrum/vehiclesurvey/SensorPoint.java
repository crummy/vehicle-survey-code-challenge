package com.malcolmcrum.vehiclesurvey;

import java.util.Objects;

class SensorPoint {
	final char sensor;
	final long millis;

	SensorPoint(char sensor, long millis) {
		this.sensor = sensor;
		this.millis = millis;
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
}
