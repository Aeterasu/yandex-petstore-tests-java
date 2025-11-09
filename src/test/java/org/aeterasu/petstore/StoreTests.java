package org.aeterasu.petstore;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.json.*;

import java.net.http.HttpResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StoreTests
{
    @Test
    @Order(11)
    public void testGetInventory() throws Exception
    {
		HttpResponse<String> response = HttpUtils.get(ApiInfo.BASE_URL + "/store/inventory/");
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
			.put("petId", TestingUtils.getRandomId())
			.put("quantity", 99)
			.put("shipDate", formattedDateTime)
            .put("status", "placed")
            .put("complete", true);

		HttpResponse<String> response = HttpUtils.postJson(ApiInfo.BASE_URL + "/store/order/", json.toString());
		assertEquals(200, response.statusCode());
	}

	@Test
	@Order(13)
	public void testGetOrderById() throws Exception
	{
		HttpResponse<String> response = HttpUtils.get(ApiInfo.BASE_URL + "/store/order/5");
		assertEquals(200, response.statusCode());
	}

	@Test
	@Order(14)
	public void testDeleteOrder() throws Exception
	{
		HttpResponse<String> response = HttpUtils.delete(ApiInfo.BASE_URL + "/store/order/1", "api_key", ApiInfo.API_KEY);
		
		assertEquals(200, response.statusCode());
	}
}