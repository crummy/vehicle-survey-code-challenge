package com.malcolmcrum.vehiclesurvey.measures;

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

	double getMeters() {
		return meters;
	}

	@Override
	public String toString() {
		return meters + "m";
	}
}
