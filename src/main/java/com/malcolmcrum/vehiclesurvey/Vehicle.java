package com.malcolmcrum.vehiclesurvey;

import java.util.Objects;

abstract class Vehicle {
	private static long WHEELBASE_MM = 2500L;
	private final Long frontWheelStart;
	private final Long frontWheelsEnd;
	private final Long rearWheelsStart;
	private final Long rearWheelsEnd;

	Vehicle(Long frontWheelsStart, Long frontWheelsEnd, Long rearWheelsStart, Long rearWheelsEnd) {
		this.frontWheelStart = Objects.requireNonNull(frontWheelsStart);
		this.frontWheelsEnd = Objects.requireNonNull(frontWheelsEnd);
		this.rearWheelsStart = rearWheelsStart;
		this.rearWheelsEnd = rearWheelsEnd;
	}

	public enum Direction {
		NORTHBOUND, SOUTHBOUND
	}
}