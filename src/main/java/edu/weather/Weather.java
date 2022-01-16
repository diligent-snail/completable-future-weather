package edu.weather;

import java.util.Objects;

public record Weather(
		String description, String city
) {
	public Weather {
		Objects.requireNonNull(description, "description == null");
		Objects.requireNonNull(city, "city == null");
	}
}
