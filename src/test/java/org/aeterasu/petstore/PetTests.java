package org.aeterasu.petstore;

import java.net.URI; 
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

public class PetTests
{
	@ParameterizedTest
	@ValueSource(ints = {-1, 0, 1, 5, 15, Integer.MAX_VALUE})
	void getPetTest(int id) throws Exception
	{
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(Api.BASE_URL + "/pet/" + Integer.toString(id)))
			.GET()
			.build();

		HttpClient httpClient = HttpClient.newHttpClient();
		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

		assertEquals(response.statusCode(), 200);
	}
}