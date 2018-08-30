package com.malcolmcrum.vehiclesurvey;

import java.util.List;

public class Survey {
	private static long WHEELBASE_MM = 2500L;

	private final List<Vehicle> vehicles;

	public Survey(List<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}
}
