package edu.weather;

import java.util.Objects;

public record Location(
		String city,
		double latitude,
		double longitude
) {
	public Location {
		Objects.requireNonNull(city, "city == null");
	}
}
