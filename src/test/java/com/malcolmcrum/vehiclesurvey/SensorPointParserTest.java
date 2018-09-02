package com.malcolmcrum.vehiclesurvey;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SensorPointParserTest {

	@Test
	public void multipleSensors() {
		List<String> sample = listOf("A0", "B1", "A9", "B20");

		List<SensorPoint> points = new SensorPointParser(sample).getPoints();

		assertThat(points).containsSequence(
				new SensorPoint('A', 0L, 0),
				new SensorPoint('B', 1L, 0),
				new SensorPoint('A', 9L, 0),
				new SensorPoint('B', 20L, 0)
		);
	}

	@Test
	public void singleDataPoint() {
		List<String> sample = listOf("A0");

		List<SensorPoint> points = new SensorPointParser(sample).getPoints();

		assertThat(points).containsOnly(new SensorPoint('A', 0L, 0));
	}

	@Test
	public void emptyData() {
		List<String> noData = listOf();

		List<SensorPoint> points = new SensorPointParser(noData).getPoints();

		assertThat(points).isEmpty();
	}

	@Test
	public void missingFile_throwIoException() {
		assertThatThrownBy(() -> SensorPointParser.parse(Paths.get("missing-file.txt")))
				.isInstanceOf(IOException.class);
	}

	static List<String> listOf (String... items) {
		return Arrays.stream(items).collect(Collectors.toList());
	}
}