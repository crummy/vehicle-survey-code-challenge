package com.malcolmcrum.vehiclesurvey;

import com.malcolmcrum.vehiclesurvey.SensorData.Point;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SensorDataTest {

	@Test
	public void emptyList() {
		List<String> noData = Collections.emptyList();

		List<Point> points = new SensorData(noData).getPoints();

		assertThat(points).isEmpty();
	}

	@Test
	public void missingFile_throwIoException() {
		assertThatThrownBy(() -> SensorData.parse(Paths.get("missing-file.txt")))
				.isInstanceOf(IOException.class);
	}
}