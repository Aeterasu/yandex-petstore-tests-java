package org.aeterasu.petstore;

import java.net.URI;
import java.net.http.HttpRequest;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.json.*;

import java.net.http.HttpResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StoreTests
{
    private static final int PET_ID = 999;

    // TODO: move consts like api key into its own container
    private static final String API_KEY = "special-key";

    @Test
    @Order(11)
    public void testGetInventory() throws Exception
    {
		HttpResponse<String> response = HttpUtils.get(HttpUtils.BASE_URL + "/store/inventory/");
		assertEquals(200, response.statusCode());        
    }

	@Test
	@Order(12)
	public void testPostOrder() throws Exception
	{
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        String formattedDateTime = currentDateTime.format(formatter);

		JSONObject json = new JSONObject()
            .put("id", 1)
			.put("petId", PET_ID)
			.put("quantity", 99)
			.put("shipDate", formattedDateTime)
            .put("status", "placed")
            .put("complete", true);

		HttpResponse<String> response = HttpUtils.post(HttpUtils.BASE_URL + "/store/order/", json.toString(), HttpUtils.APPLICATION_JSON);
		assertEquals(200, response.statusCode());
	}

	@Test
	@Order(13)
	public void testGetOrderById() throws Exception
	{
		HttpResponse<String> response = HttpUtils.get(HttpUtils.BASE_URL + "/store/order/5");
		assertEquals(200, response.statusCode());
	}

	@Test
	@Order(14)
	public void testDeleteOrder() throws Exception
	{
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(HttpUtils.BASE_URL + "/store/order/1"))
				.header("api_key", API_KEY)
				.DELETE()
				.build();

		HttpResponse<String> response = HttpUtils.client.send(request, HttpResponse.BodyHandlers.ofString());
		
		assertEquals(200, response.statusCode());
	}
}