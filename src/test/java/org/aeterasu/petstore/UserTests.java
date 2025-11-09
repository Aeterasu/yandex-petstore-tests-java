package org.aeterasu.petstore;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.json.*;

import java.net.http.HttpResponse;
import java.util.UUID;

import org.aeterasu.petstore.user.User;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTests
{
	private static long testUserId = 0;

	private static final String USERNAME = "Kartoshka";
	private static final String PASSWORD = "1234567890";

	@BeforeAll
	public static void initId()
	{
		testUserId = TestingUtils.getRandomId();
	}

	private JSONObject makeUser(String username, String password) 
	{
		long id = TestingUtils.getRandomId();
		return new JSONObject()
				.put("id", id)
				.put("username", username)
				.put("firstName", "Fn")
				.put("lastName", "Ln")
				.put("email", username + "@example.com")
				.put("password", password)
				.put("phone", "1234567890")
				.put("userStatus", 0);
	}

	@Test
	@Order(21)
	public void testPostCreateUser() throws Exception
	{
		JSONObject json = new JSONObject()
			.put("id", testUserId)
			.put("username", USERNAME)
			.put("firstName", "Ivan")
			.put("lastName", "Invanovich")
			.put("email", "kartoshka@kartoshka.com")
			.put("password", PASSWORD)
			.put("phone", "1234567890")
			.put("userStatus", 0);

		HttpResponse<String> response = HttpUtils.postJson(Api.BASE_URL + "/user/", json.toString());
		assertEquals(200, response.statusCode());
	}

	@Test
	@Order(22)
	public void testLogin() throws Exception
	{
		HttpResponse<String> response = User.login(USERNAME, PASSWORD);
		assertEquals(200, response.statusCode());
	}

	@Test
	@Order(23)
	public void testPutUpdateUser() throws Exception
	{
		JSONObject json = new JSONObject()
			.put("id", testUserId)
			.put("username", USERNAME)
			.put("firstName", "Andrey")
			.put("lastName", "Andreevich")
			.put("email", "kartoshka@kartoshka.com")
			.put("password", "1234567890")
			.put("phone", "1234567890")
			.put("userStatus", 0);

		HttpResponse<String> response = HttpUtils
			.putJson(Api.BASE_URL + "/user/" + USERNAME, json.toString());
		assertEquals(200, response.statusCode());
	}

	// endpoint returns either 200 or 404 at random - too bad!
	// we bash our head against getting user until something happens
	public static HttpResponse<String> getUserWithRetry(int maxRetries, int delayMs) throws Exception {
		int attempts = 0;
		HttpResponse<String> response = null;

		while (attempts < maxRetries) 
		{
			response = HttpUtils.get(Api.BASE_URL + "/user/user1");

			if (response.statusCode() == 200) 
			{
				return response;
			}

			attempts++;

			Thread.sleep(delayMs);
		}

		// either 200 or 404 - a wild world of endless possibilities!
		return response;
	}

	@Test
	@Order(24)
	void testGetUser() throws Exception 
	{
		HttpResponse<String> response = getUserWithRetry(5, 500); // 5 retries, 0.5s apart

		assertEquals(200, response.statusCode());
	}

	@Test
	@Order(25)
	public void testLogout() throws Exception
	{
		HttpResponse<String> response = User.logout();
		assertEquals(200, response.statusCode());
	}

	@Test
	@Order(26)
	public void testDeleteUser() throws Exception
	{
		HttpResponse<String> loginResponse = User.login(USERNAME, PASSWORD);
		assertEquals(200, loginResponse.statusCode());

		HttpResponse<String> response = HttpUtils
			.delete(Api.BASE_URL + "/user/" + USERNAME);
		
		assertEquals(200, response.statusCode());
	}

	@Test
	@Order(27)
	public void testCreateUsersWithArray() throws Exception
	{
		String nameA = "userA_" + UUID.randomUUID().toString().substring(0, 6);
		String nameB = "userB_" + UUID.randomUUID().toString().substring(0, 6);
		String password = "password";

		JSONObject userA = makeUser(nameA, password);
		JSONObject userB = makeUser(nameB, password);

		JSONArray arr = new JSONArray();
		arr.put(userA);
		arr.put(userB);

		// no, I have no idea why there's two identical methods with different names!

		// createWithArray
		HttpResponse<String> respArray = HttpUtils
			.postJson(Api.BASE_URL + "/user/createWithArray/", arr.toString());

		assertEquals(200, respArray.statusCode());

		// createWithList
		HttpResponse<String> respList = HttpUtils
			.postJson(Api.BASE_URL + "/user/createWithList/", arr.toString());

		assertEquals(200, respList.statusCode());
	}
}