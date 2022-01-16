package edu.weather;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpBasedLocationService implements LocationService {
	private static final URI IP_INFO_URI = URI.create("https://ipinfo.io/json");
	private final HttpClient httpClient;

	public IpBasedLocationService(HttpClient httpClient) {
		this.httpClient = Objects.requireNonNull(httpClient, "httpClient == null");
	}

	@Override
	public CompletableFuture<Location> getCurrentLocation() {
		HttpRequest request = HttpRequest
				.newBuilder()
				.uri(IP_INFO_URI)
				.build();
		return httpClient
				.sendAsync(request, HttpResponse.BodyHandlers.ofString())
				.thenApply(HttpResponse::body)
				.thenApply(this::extractLocation);
	}

	// Волшебный метод, который из JSON'а извлечет локацию
	private Location extractLocation(String body) {
		Matcher matcher = Pattern.compile("\"city\"\\s*:\\s*\"([^\"]*)\"").matcher(body);
		//noinspection ResultOfMethodCallIgnored
		matcher.find();
		String city = matcher.group(1);
		Matcher coordinatesMatcher = Pattern.compile("\"loc\"\\s*:\\s*\"([^\"]*)\"").matcher(body);
		//noinspection ResultOfMethodCallIgnored
		coordinatesMatcher.find();
		String location = coordinatesMatcher.group(1);
		String[] coordinates = location.split(",");
		return new Location(city, Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1]));
	}
}
