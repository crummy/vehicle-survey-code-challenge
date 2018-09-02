package com.malcolmcrum.vehiclesurvey.measures;

import java.time.Duration;
import java.util.Objects;

/**
 * A helper class to store speed in a consistent manner to allow conversion between units without confusion.
 */
public class Speed implements Comparable<Speed> {
	public static final Speed ZERO = new Speed(0);
	private final double metersPerSecond;

	public Speed(Length length, Duration time) {
		this.metersPerSecond = length.getMeters() * time.getSeconds();
	}

	public Speed(double metersPerSecond) {
		this.metersPerSecond = metersPerSecond;
	}

	public double getMetersPerSecond() {
		return metersPerSecond;
	}

	public Speed plus(Speed other) {
		return new Speed(metersPerSecond + other.metersPerSecond);
	}

	public double getKilometersPerHour() {
		return metersPerSecond * 60 * 60 / 1000;
	}

	@Override
	public int compareTo(Speed o) {
		return Double.compare(this.metersPerSecond, o.metersPerSecond);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Speed speed = (Speed) o;
		return Double.compare(speed.metersPerSecond, metersPerSecond) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(metersPerSecond);
	}
}
