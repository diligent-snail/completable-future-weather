package edu.weather;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultWeatherService implements WeatherService {
	private static final Pattern WEATHER_DESCRIPTION_PATTERN = Pattern.compile(".*\"weather\"\\s*:\\s*\"([^\"]*)\"");

	private final HttpClient httpClient;

	public DefaultWeatherService(HttpClient httpClient) {
		this.httpClient = Objects.requireNonNull(httpClient, "httpClient == null");
	}

	@Override
	public CompletableFuture<Weather> getWeather(Location location) {
		URI uri = URI.create("http://www.7timer.info/bin/api.pl?"
				+ "lon=" + location.longitude()
				+ "&lat=" + location.latitude()
				+ "&product=civillight&output=json");
		HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
		return httpClient
				.sendAsync(request, HttpResponse.BodyHandlers.ofString())
				.thenApply(HttpResponse::body)
				.thenApply(response -> extractWeather(location, response));
	}

	private Weather extractWeather(Location location, String s) {
		Matcher matcher = WEATHER_DESCRIPTION_PATTERN.matcher(s);
		if (!matcher.find()) {
			throw new IllegalStateException("Unable to extract weather from " + s);
		}
		String description = matcher.group(1);
		return new Weather(description, location.city());
	}
}
