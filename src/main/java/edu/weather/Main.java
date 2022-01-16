package edu.weather;

import java.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {
	private final HttpClient httpClient = HttpClient
			.newBuilder()
			.followRedirects(HttpClient.Redirect.ALWAYS)
			.build();
	private final LocationService locationService = new IpBasedLocationService(httpClient);
	private final WeatherService weatherService = new DefaultWeatherService(httpClient);


	public void run() throws ExecutionException, InterruptedException {
		CompletableFuture<Location> currentLocation = locationService.getCurrentLocation();
		currentLocation
				.whenComplete((location, throwable) -> System.out.println("You are in " + location.city()))
				.thenCompose(weatherService::getWeather)
				.thenAccept(weather -> System.out.println("It is " + weather.description() + " in " + weather.city()))
				.get();
	}

	public static void main(String[] args) throws ExecutionException, InterruptedException {
		new Main().run();
	}
}
