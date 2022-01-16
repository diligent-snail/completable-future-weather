package edu.weather;

import java.util.concurrent.CompletableFuture;

public interface WeatherService {
	CompletableFuture<Weather> getWeather(Location location);
}
