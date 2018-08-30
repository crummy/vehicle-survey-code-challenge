package com.malcolmcrum.vehiclesurvey;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SensorPointFactoryTest {

	@Test
	public void multipleSensors() {
		List<String> sample = listOf("A0", "B1", "A9", "B20");

		List<SensorPoint> points = new SensorPointFactory(sample).getPoints();

		assertThat(points).containsSequence(
				new SensorPoint('A', 0L),
				new SensorPoint('B', 1L),
				new SensorPoint('A', 9L),
				new SensorPoint('B', 20L)
		);
	}

	@Test
	public void singleDataPoint() {
		List<String> sample = listOf("A0");

		List<SensorPoint> points = new SensorPointFactory(sample).getPoints();

		assertThat(points).containsOnly(new SensorPoint('A', 0L));
	}

	@Test
	public void emptyData() {
		List<String> noData = listOf();

		List<SensorPoint> points = new SensorPointFactory(noData).getPoints();

		assertThat(points).isEmpty();
	}

	@Test
	public void missingFile_throwIoException() {
		assertThatThrownBy(() -> SensorPointFactory.parse(Paths.get("missing-file.txt")))
				.isInstanceOf(IOException.class);
	}

	private List<String> listOf (String... items) {
		return Arrays.stream(items).collect(Collectors.toList());
	}
}