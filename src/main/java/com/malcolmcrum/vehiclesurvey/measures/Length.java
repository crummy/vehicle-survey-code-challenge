package com.malcolmcrum.vehiclesurvey.measures;

/**
 * A helper class to store distance in a consistent manner to allow conversion between units without confusion.
 */
public class Length {
	public static final Length METER = new Length(1);

	private final double meters;

	private Length(double meters) {
		this.meters = meters;
	}

	public static Length fromMillimeters(double mm) {
		return new Length(mm * 1000);
	}

	public static Length fromMeters(double m) {
		return new Length(m);
	}

	public double getMeters() {
		return meters;
	}
}
