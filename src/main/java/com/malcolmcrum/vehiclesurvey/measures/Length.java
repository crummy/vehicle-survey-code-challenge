package com.malcolmcrum.vehiclesurvey.measures;

import java.util.Objects;

/**
 * A helper class to store distance in a consistent manner to allow conversion between units without confusion.
 */
public class Length {
	static final Length METER = new Length(1);

	private final double meters;

	private Length(double meters) {
		this.meters = meters;
	}

	public static Length fromMeters(double m) {
		return new Length(m);
	}

	public static Length fromKilometers(double km) {
		return new Length(km * 1000);
	}

	public double getMeters() {
		return meters;
	}

	public Length minus(Length other) {
		return new Length(this.meters - other.meters);
	}

	@Override
	public String toString() {
		return String.format("%.1fm", meters);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Length length = (Length) o;
		return Double.compare(length.meters, meters) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(meters);
	}
}
