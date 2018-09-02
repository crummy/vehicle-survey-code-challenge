package com.malcolmcrum.vehiclesurvey.measures;

import java.time.Duration;

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
}
