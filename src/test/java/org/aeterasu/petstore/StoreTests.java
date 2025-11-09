package org.aeterasu.petstore;
import org.aeterasu.petstore.store.StoreOrder;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.net.http.HttpResponse;

public class StoreTests
{
	@Test
	public void testGetInventory() throws Exception
	{
		HttpResponse<String> response = HttpUtils.get(Api.BASE_URL + "/store/inventory/");
		assertEquals(200, response.statusCode());        
	}

	@Test
	public void testPostOrder() throws Exception
	{
		long r = (long)(Math.random() * 10) + 1;

		StoreOrder order = new StoreOrder(
			r,
			TestingUtils.getRandomId(),
			99,
			StoreOrder.getCurrentDataFormatted(),
			"placed",
			true
		);

		HttpResponse<String> response = HttpUtils.postJson(Api.BASE_URL + "/store/order/", order.getJson().toString());
		assertEquals(200, response.statusCode());
	}

	@Test
	public void testPostInvalidOrder() throws Exception
	{
		HttpResponse<String> response = HttpUtils.postJson(Api.BASE_URL + "/store/order/", "");
		assertEquals(400, response.statusCode());
	}

	@Nested
	@SuppressWarnings("unused")
	class ExistingOrderTests
	{
		private StoreOrder testOrder = null;
		private long orderId = 0;

		@BeforeEach
		public void setupOrder() throws Exception
		{
			long r = (long)(Math.random() * 10) + 1;

			orderId = r;
			testOrder = new StoreOrder(
				r,
				TestingUtils.getRandomId(),
				99,
				StoreOrder.getCurrentDataFormatted(),
				"placed",
				true
			);

			HttpResponse<String> postResponse = HttpUtils.postJson(Api.BASE_URL + "/store/order", testOrder.getJson().toString());
			assertEquals(200, postResponse.statusCode());
		}

		@Test
		public void testGetOrderById() throws Exception
		{
			TestingUtils.pollAwait(() ->
			{
					HttpResponse<String> getResponse = HttpUtils.get(Api.BASE_URL + "/store/order/" + Long.toString(orderId));
					assertEquals(200, getResponse.statusCode());
			});
		}

		@Test
		public void testDeleteOrder() throws Exception
		{
			TestingUtils.pollAwait(() ->
			{
				HttpResponse<String> response = HttpUtils.delete(Api.BASE_URL + "/store/order/" + Long.toString(orderId));
				assertEquals(200, response.statusCode());
			});
		}

		@Test
		public void testGetDeletedOrderById() throws Exception
		{
			TestingUtils.pollAwait(() ->
			{
				HttpResponse<String> response = HttpUtils.delete(Api.BASE_URL + "/store/order/" + Long.toString(orderId));
				assertEquals(200, response.statusCode());
			});

			TestingUtils.pollAwait(() ->
			{
				HttpResponse<String> getResponse = HttpUtils.get(Api.BASE_URL + "/store/order/" + Long.toString(orderId));
				assertEquals(404, getResponse.statusCode());
			});
		}
	}
}