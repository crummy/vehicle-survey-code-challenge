package com.malcolmcrum.vehiclesurvey;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class FileReader {
	List<String> parse(String file) throws IOException {
		Path path = Paths.get(file);
		try (Stream<String> lines = Files.lines(path)) {
			return lines.collect(Collectors.toList());
		}
	}
}
