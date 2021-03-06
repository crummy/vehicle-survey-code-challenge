package com.malcolmcrum.vehiclesurvey.measures;

import java.time.Duration;
import java.util.Objects;

/**
 * A helper class to store speed in a consistent manner to allow conversion between units without confusion.
 */
public class Speed implements Comparable<Speed> {
	private final double metersPerSecond;

	public Speed(Length length, Duration time) {
		this.metersPerSecond = length.getMeters() / time.toMillis() * 1000;
	}

	public Speed(double metersPerSecond) {
		this.metersPerSecond = metersPerSecond;
	}

	public static Speed fromKph(double kph) {
		return new Speed(Length.fromKilometers(kph), Duration.ofHours(1));
	}

	public double getMetersPerSecond() {
		return metersPerSecond;
	}

	public double getKilometersPerHour() {
		return metersPerSecond * 60 * 60 / 1000;
	}

	public Duration toDuration(Length length) {
		double seconds = length.getMeters() / metersPerSecond;
		double milliseconds = seconds * 1000;
		return Duration.ofMillis((long)milliseconds);
	}

	public Length toDistance(Duration duration) {
		double millimeters = metersPerSecond * duration.toMillis();
		double meters = millimeters / 1000;
		return Length.fromMeters(meters);
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

	@Override
	public String toString() {
		return String.format("%.1fkph", getKilometersPerHour());
	}
}
