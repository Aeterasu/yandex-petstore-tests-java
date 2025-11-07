package org.aeterasu.petstore;

import java.net.URI;
import java.net.http.HttpRequest;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.json.*;

import java.net.http.HttpResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.MethodOrderer.*;

@TestMethodOrder(OrderAnnotation.class)
public class StoreTests
{
    private static final int PET_ID = 999;
    private static final int ORDER_ID = 999;

    // TODO: move consts like api key into its own container
    private static final String API_KEY = "special-key";

    @Test
    @Order(1)
    public void testGetInventory() throws Exception
    {
		HttpResponse<String> response = HttpUtils.get(HttpUtils.BASE_URL + "/store/inventory/");
		assertEquals(200, response.statusCode());        
    }

	@Test
	@Order(2)
	public void testPostOrder() throws Exception
	{
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        String formattedDateTime = currentDateTime.format(formatter);

		JSONObject json = new JSONObject()
            .put("id", ORDER_ID)
			.put("petId", PET_ID)
			.put("quantity", 99)
			.put("shipDate", formattedDateTime)
            .put("status", "placed")
            .put("complete", true);

		HttpResponse<String> response = HttpUtils.post(HttpUtils.BASE_URL + "/store/order/", json.toString(), HttpUtils.APPLICATION_JSON);
		assertEquals(200, response.statusCode());
	}

	@Test
	@Order(3)
	public void testGetPetById() throws Exception
	{
		HttpResponse<String> response = HttpUtils.get(HttpUtils.BASE_URL + "/store/order/" + Integer.toString(ORDER_ID));
		assertEquals(200, response.statusCode());
	}

	@Test
	@Order(4)
	public void testDeletePet() throws Exception
	{
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(HttpUtils.BASE_URL + "/store/order/" + ORDER_ID))
				.header("api_key", API_KEY)
				.DELETE()
				.build();

		HttpResponse<String> response = HttpUtils.client.send(request, HttpResponse.BodyHandlers.ofString());
		
		assertEquals(200, response.statusCode());
	}
}