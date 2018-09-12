package com.malcolmcrum.vehiclesurvey;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListHelper {
	@SafeVarargs
	static <T> List<T> listOf(T... elements) {
		return Arrays.stream(elements).collect(Collectors.toList());
	}

}
