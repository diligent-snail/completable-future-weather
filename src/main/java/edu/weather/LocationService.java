package edu.weather;

import java.util.concurrent.CompletableFuture;

public interface LocationService {
	CompletableFuture<Location> getCurrentLocation();
}
